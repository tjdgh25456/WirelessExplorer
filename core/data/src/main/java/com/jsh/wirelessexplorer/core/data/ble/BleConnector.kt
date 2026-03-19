package com.jsh.wirelessexplorer.core.data.ble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothProfile
import android.content.Context
import com.jsh.wirelessexplorer.core.common.IoDispatcher
import com.jsh.wirelessexplorer.core.model.BleConnectedDevice
import com.jsh.wirelessexplorer.core.model.BleConnectionState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ClosedSendChannelException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import timber.log.Timber
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject
import javax.inject.Singleton

private const val GATT_ERROR_133 = 133
private const val MAX_RETRY_COUNT = 3
private const val OP_QUEUE_DELAY_MS = 50L
private const val OP_TIMEOUT_MS = 5_000L
private const val RSSI_POLL_INTERVAL_MS = 2_000L
private const val CHARACTERISTIC_UPDATE_BUFFER_CAPACITY = 64
private const val CCC_DESCRIPTOR_UUID = "00002902-0000-1000-8000-00805f9b34fb"

@Singleton
class BleConnector @Inject constructor(
    @ApplicationContext private val context: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val bluetoothAdapter: BluetoothAdapter?,
) {

    @Volatile
    private var gattRef: BluetoothGatt? = null

    // 연결마다 새로 생성되는 GATT 오퍼레이션 직렬 큐
    private val activeQueue = AtomicReference<Channel<GattOperation>?>(null)

    // GATT 수명 관리용 CoroutineScope (큐 프로세서, RSSI 폴링)
    @Volatile
    private var connectionScope: CoroutineScope? = null
    private val retryCount = AtomicInteger(0)

    // GATT 연결 상태 (ViewModel 수명과 무관하게 유지)
    private val _connectionState = MutableStateFlow<BleConnectionState>(BleConnectionState.Idle)
    val connectionState: StateFlow<BleConnectionState> = _connectionState

    // 현재 연결된 디바이스 추적
    private val connectedDevices = ConcurrentHashMap<String, BleConnectedDevice>()
    private val _connectedDevicesFlow = MutableStateFlow<List<BleConnectedDevice>>(emptyList())
    val connectedDevicesFlow: StateFlow<List<BleConnectedDevice>> = _connectedDevicesFlow

    // Characteristic 변경 알림 스트림
    private val _characteristicUpdates = MutableSharedFlow<CharacteristicUpdate>(
        extraBufferCapacity = CHARACTERISTIC_UPDATE_BUFFER_CAPACITY,
    )
    val characteristicUpdates: SharedFlow<CharacteristicUpdate> = _characteristicUpdates

    // 현재 진행 중인 오퍼레이션의 결과를 수신하기 위한 deferred (AtomicReference로 스레드 안전 보장)
    private val pendingRead = AtomicReference<CompletableDeferred<Result<ByteArray>>?>(null)
    private val pendingWrite = AtomicReference<CompletableDeferred<Result<Unit>>?>(null)
    private val pendingDescriptorWrite = AtomicReference<CompletableDeferred<Result<Unit>>?>(null)

    private val gattCallback = object : BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            when {
                status == GATT_ERROR_133 && retryCount.get() < MAX_RETRY_COUNT -> {
                    val attempt = retryCount.incrementAndGet()
                    Timber.d("GATT error 133 발생, 재시도 ($attempt/$MAX_RETRY_COUNT)")
                    gatt.close()
                    gattRef = gatt.device.let { device ->
                        device.connectGatt(context, false, this, BluetoothDevice.TRANSPORT_LE)
                    }
                }

                newState == BluetoothProfile.STATE_CONNECTED && status == BluetoothGatt.GATT_SUCCESS -> {
                    Timber.d("GATT 연결 성공: ${gatt.device.address}")
                    retryCount.set(0)
                    trackDeviceConnected(gatt.device)
                    _connectionState.value = BleConnectionState.DiscoveringServices
                    gatt.discoverServices()
                }

                newState == BluetoothProfile.STATE_DISCONNECTED -> {
                    trackDeviceDisconnected(gatt.device.address)
                    if (status != BluetoothGatt.GATT_SUCCESS) {
                        Timber.e("GATT 연결 해제 (status=$status)")
                        _connectionState.value = BleConnectionState.Error("Disconnected with error", status)
                    } else {
                        Timber.d("GATT 정상 연결 해제")
                        _connectionState.value = BleConnectionState.Idle
                    }
                    cleanupPendingOperations()
                }
            }
        }

        @SuppressLint("MissingPermission")
        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Timber.d("GATT 서비스 발견 완료: ${gatt.services.size}개")
                val services = gatt.services.map { it.toGattServiceInfo() }
                val addr = gatt.device.address
                connectedDevices[addr]?.let { existing ->
                    connectedDevices[addr] = existing.copy(serviceCount = services.size)
                    _connectedDevicesFlow.value = connectedDevices.values.toList()
                }
                _connectionState.value = BleConnectionState.Connected(services)

                // 서비스 발견 후 RSSI 폴링 시작
                connectionScope?.launch {
                    while (true) {
                        delay(RSSI_POLL_INTERVAL_MS)
                        gattRef?.readRemoteRssi()
                    }
                }
            } else {
                Timber.e("GATT 서비스 발견 실패: status=$status")
                _connectionState.value = BleConnectionState.Error("Service discovery failed", status)
            }
        }

        @Deprecated("Deprecated in API 33+")
        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int,
        ) {
            handleCharacteristicRead(characteristic.value, status)
        }

        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray,
            status: Int,
        ) {
            handleCharacteristicRead(value, status)
        }

        override fun onCharacteristicWrite(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int,
        ) {
            val deferred = pendingWrite.getAndSet(null) ?: return
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Timber.d("Characteristic 쓰기 성공: ${characteristic.uuid}")
                deferred.safeComplete(Result.success(Unit))
            } else {
                Timber.e("Characteristic 쓰기 실패: status=$status")
                deferred.safeComplete(Result.failure(Exception("Write failed with status: $status")))
            }
        }

        @Deprecated("Deprecated in API 33+")
        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
        ) {
            handleCharacteristicChanged(characteristic.uuid.toString(), characteristic.value)
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray,
        ) {
            handleCharacteristicChanged(characteristic.uuid.toString(), value)
        }

        override fun onDescriptorWrite(
            gatt: BluetoothGatt,
            descriptor: BluetoothGattDescriptor,
            status: Int,
        ) {
            val deferred = pendingDescriptorWrite.getAndSet(null) ?: return
            if (status == BluetoothGatt.GATT_SUCCESS) {
                deferred.safeComplete(Result.success(Unit))
            } else {
                deferred.safeComplete(
                    Result.failure(Exception("Descriptor write failed: $status")),
                )
            }
        }

        override fun onReadRemoteRssi(gatt: BluetoothGatt, rssi: Int, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                updateConnectedDeviceRssi(gatt.device.address, rssi)
            }
        }
    }

    /**
     * BLE 디바이스에 GATT 연결을 수립하고 연결 상태 Flow를 반환한다.
     * GATT 수명은 이 Flow의 수집 여부와 무관하게 유지된다.
     * 연결 해제는 반드시 [disconnect]를 통해서만 수행한다.
     */
    @SuppressLint("MissingPermission")
    fun connect(address: String): Flow<BleConnectionState> {
        val current = _connectionState.value
        val currentAddress = gattRef?.device?.address

        // 같은 디바이스에 이미 연결 중이면 현재 상태를 그대로 관찰
        if (currentAddress == address &&
            (current is BleConnectionState.Connecting ||
                current is BleConnectionState.DiscoveringServices ||
                current is BleConnectionState.Connected)
        ) {
            return connectionState
        }

        // 다른 디바이스 연결 요청 → 기존 연결 정리
        if (currentAddress != null && currentAddress != address) {
            disconnect()
        }

        val device = bluetoothAdapter?.getRemoteDevice(address)
        if (device == null) {
            _connectionState.value = BleConnectionState.Error("Device not found", -1)
            return connectionState
        }

        retryCount.set(0)
        _connectionState.value = BleConnectionState.Connecting

        // 이전 연결 리소스 정리
        connectionScope?.cancel()
        activeQueue.getAndSet(null)?.close()

        val scope = CoroutineScope(ioDispatcher + SupervisorJob())
        connectionScope = scope

        val queue = Channel<GattOperation>(Channel.BUFFERED)
        activeQueue.set(queue)

        scope.launch {
            for (operation in queue) {
                delay(OP_QUEUE_DELAY_MS)
                operation.execute()
            }
        }

        gattRef = device.connectGatt(context, false, gattCallback, BluetoothDevice.TRANSPORT_LE)

        return connectionState
    }

    @SuppressLint("MissingPermission")
    fun disconnect() {
        cleanupPendingOperations()
        connectionScope?.cancel()
        connectionScope = null
        activeQueue.getAndSet(null)?.close()
        val address = gattRef?.device?.address
        gattRef?.let { gatt ->
            gatt.disconnect()
            gatt.close()
        }
        gattRef = null
        if (address != null) trackDeviceDisconnected(address)
        _connectionState.value = BleConnectionState.Idle
    }

    /**
     * GATT Characteristic 읽기 오퍼레이션.
     * 직렬 큐를 통해 순서대로 실행되며, 타임아웃은 5초.
     */
    @SuppressLint("MissingPermission")
    suspend fun readCharacteristic(serviceUuid: String, charUuid: String): Result<ByteArray> =
        withContext(ioDispatcher) {
            val gatt = gattRef ?: return@withContext Result.failure(Exception("Not connected"))
            val characteristic = findCharacteristic(gatt, serviceUuid, charUuid)
                ?: return@withContext Result.failure(Exception("Characteristic not found"))

            val deferred = CompletableDeferred<Result<ByteArray>>()
            if (!pendingRead.compareAndSet(null, deferred)) {
                return@withContext Result.failure(Exception("Read already in progress"))
            }

            try {
                activeQueue.get()?.trySend(GattOperation { gatt.readCharacteristic(characteristic) })
            } catch (e: ClosedSendChannelException) {
                pendingRead.compareAndSet(deferred, null)
                return@withContext Result.failure(Exception("Operation queue closed"))
            }

            withTimeoutOrNull(OP_TIMEOUT_MS) { deferred.await() }
                ?: Result.failure<ByteArray>(Exception("Read operation timed out")).also {
                    pendingRead.compareAndSet(deferred, null)
                }
        }

    /**
     * GATT Characteristic 쓰기 오퍼레이션.
     */
    @SuppressLint("MissingPermission")
    suspend fun writeCharacteristic(
        serviceUuid: String,
        charUuid: String,
        value: ByteArray,
    ): Result<Unit> = withContext(ioDispatcher) {
        val gatt = gattRef ?: return@withContext Result.failure(Exception("Not connected"))
        val characteristic = findCharacteristic(gatt, serviceUuid, charUuid)
            ?: return@withContext Result.failure(Exception("Characteristic not found"))

        val deferred = CompletableDeferred<Result<Unit>>()
        if (!pendingWrite.compareAndSet(null, deferred)) {
            return@withContext Result.failure(Exception("Write already in progress"))
        }

        try {
            activeQueue.get()?.trySend(
                GattOperation {
                    gatt.writeCharacteristic(
                        characteristic,
                        value,
                        BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT,
                    )
                },
            )
        } catch (e: ClosedSendChannelException) {
            pendingWrite.compareAndSet(deferred, null)
            return@withContext Result.failure(Exception("Operation queue closed"))
        }

        withTimeoutOrNull(OP_TIMEOUT_MS) { deferred.await() }
            ?: Result.failure<Unit>(Exception("Write operation timed out")).also {
                pendingWrite.compareAndSet(deferred, null)
            }
    }

    /**
     * Characteristic Notification 활성화/비활성화.
     */
    @SuppressLint("MissingPermission")
    suspend fun setNotification(
        serviceUuid: String,
        charUuid: String,
        enable: Boolean,
    ): Result<Unit> = withContext(ioDispatcher) {
        val gatt = gattRef ?: return@withContext Result.failure(Exception("Not connected"))
        val characteristic = findCharacteristic(gatt, serviceUuid, charUuid)
            ?: return@withContext Result.failure(Exception("Characteristic not found"))

        val descriptor = characteristic.getDescriptor(UUID.fromString(CCC_DESCRIPTOR_UUID))
            ?: return@withContext Result.failure(Exception("CCC descriptor not found"))

        val deferred = CompletableDeferred<Result<Unit>>()
        if (!pendingDescriptorWrite.compareAndSet(null, deferred)) {
            return@withContext Result.failure(Exception("Descriptor write already in progress"))
        }

        val descriptorValue = if (enable) {
            BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
        } else {
            BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE
        }

        try {
            activeQueue.get()?.trySend(
                GattOperation {
                    val success = gatt.setCharacteristicNotification(characteristic, enable)
                    if (success) {
                        gatt.writeDescriptor(descriptor, descriptorValue)
                    } else {
                        Timber.e("setCharacteristicNotification 실패: ${characteristic.uuid}")
                        val d = pendingDescriptorWrite.getAndSet(null)
                        d?.safeComplete(
                            Result.failure(Exception("setCharacteristicNotification failed")),
                        )
                    }
                },
            )
        } catch (e: ClosedSendChannelException) {
            pendingDescriptorWrite.compareAndSet(deferred, null)
            return@withContext Result.failure(Exception("Operation queue closed"))
        }

        withTimeoutOrNull(OP_TIMEOUT_MS) { deferred.await() }
            ?: Result.failure<Unit>(Exception("Notification setup timed out")).also {
                pendingDescriptorWrite.compareAndSet(deferred, null)
            }
    }

    private fun findCharacteristic(
        gatt: BluetoothGatt,
        serviceUuid: String,
        charUuid: String,
    ): BluetoothGattCharacteristic? {
        val sUuid = runCatching { UUID.fromString(serviceUuid) }.getOrElse {
            Timber.e(it, "잘못된 서비스 UUID 형식: $serviceUuid")
            return null
        }
        val cUuid = runCatching { UUID.fromString(charUuid) }.getOrElse {
            Timber.e(it, "잘못된 Characteristic UUID 형식: $charUuid")
            return null
        }
        val service = gatt.getService(sUuid)
        return service?.getCharacteristic(cUuid)
    }

    private fun handleCharacteristicRead(value: ByteArray?, status: Int) {
        val deferred = pendingRead.getAndSet(null) ?: return
        if (status == BluetoothGatt.GATT_SUCCESS && value != null) {
            Timber.d("Characteristic 읽기 성공: ${value.size} bytes")
            deferred.safeComplete(Result.success(value))
        } else {
            Timber.e("Characteristic 읽기 실패: status=$status")
            deferred.safeComplete(Result.failure(Exception("Read failed with status: $status")))
        }
    }

    private fun handleCharacteristicChanged(uuid: String, value: ByteArray?) {
        value?.let {
            Timber.d("Characteristic 변경 알림: $uuid, ${it.size} bytes")
            _characteristicUpdates.tryEmit(CharacteristicUpdate(uuid.lowercase(), it))
        }
    }

    @SuppressLint("MissingPermission")
    private fun trackDeviceConnected(device: BluetoothDevice) {
        val serviceCount = gattRef?.services?.size ?: 0
        connectedDevices[device.address] = BleConnectedDevice(
            address = device.address,
            name = device.name,
            serviceCount = serviceCount,
        )
        _connectedDevicesFlow.value = connectedDevices.values.toList()
    }

    private fun trackDeviceDisconnected(address: String) {
        connectedDevices.remove(address)
        _connectedDevicesFlow.value = connectedDevices.values.toList()
    }

    private fun updateConnectedDeviceRssi(address: String, rssi: Int) {
        val existing = connectedDevices[address] ?: return
        connectedDevices[address] = existing.copy(rssi = rssi)
        _connectedDevicesFlow.value = connectedDevices.values.toList()
    }

    private fun cleanupPendingOperations() {
        val cancelled = Exception("Operation cancelled: disconnected")
        pendingRead.getAndSet(null)?.safeComplete(Result.failure(cancelled))
        pendingWrite.getAndSet(null)?.safeComplete(Result.failure(cancelled))
        pendingDescriptorWrite.getAndSet(null)?.safeComplete(Result.failure(cancelled))
    }

    private fun interface GattOperation {
        fun execute()
    }
}

/** 이미 완료된 Deferred에 대한 중복 완료 방지 확장 함수 */
private fun <T> CompletableDeferred<T>.safeComplete(value: T) {
    if (!isCompleted) {
        try {
            complete(value)
        } catch (e: IllegalStateException) {
            Timber.w(e, "Deferred 이미 완료됨")
        }
    }
}

data class CharacteristicUpdate(
    val characteristicUuid: String,
    val value: ByteArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CharacteristicUpdate) return false
        return characteristicUuid == other.characteristicUuid && value.contentEquals(other.value)
    }

    override fun hashCode(): Int =
        31 * characteristicUuid.hashCode() + value.contentHashCode()
}
