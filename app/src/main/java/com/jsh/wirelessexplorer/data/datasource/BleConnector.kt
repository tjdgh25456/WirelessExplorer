package com.jsh.wirelessexplorer.data.datasource

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.Context
import com.jsh.wirelessexplorer.data.mapper.toGattServiceInfo
import com.jsh.wirelessexplorer.di.IoDispatcher
import com.jsh.wirelessexplorer.domain.model.BleConnectionState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

private const val GATT_ERROR_133 = 133
private const val MAX_RETRY_COUNT = 3
private const val OP_QUEUE_DELAY_MS = 50L
private const val OP_TIMEOUT_MS = 5_000L
private const val CHARACTERISTIC_UPDATE_BUFFER_CAPACITY = 64
private const val CCC_DESCRIPTOR_UUID = "00002902-0000-1000-8000-00805f9b34fb"

@Singleton
class BleConnector @Inject constructor(
    @ApplicationContext private val context: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) {

    private val bluetoothManager by lazy {
        context.getSystemService(BluetoothManager::class.java)
    }
    private val bluetoothAdapter by lazy { bluetoothManager.adapter }

    @Volatile
    private var gattRef: BluetoothGatt? = null

    // GATT 오퍼레이션 직렬 큐
    private val operationQueue = Channel<GattOperation>(Channel.BUFFERED)

    // Characteristic 변경 알림 스트림
    private val _characteristicUpdates = MutableSharedFlow<CharacteristicUpdate>(
        extraBufferCapacity = CHARACTERISTIC_UPDATE_BUFFER_CAPACITY,
    )
    val characteristicUpdates: SharedFlow<CharacteristicUpdate> = _characteristicUpdates

    // 현재 진행 중인 오퍼레이션의 결과를 수신하기 위한 deferred
    @Volatile
    private var pendingRead: CompletableDeferred<Result<ByteArray>>? = null

    @Volatile
    private var pendingWrite: CompletableDeferred<Result<Unit>>? = null

    @Volatile
    private var pendingDescriptorWrite: CompletableDeferred<Result<Unit>>? = null

    /**
     * BLE 디바이스에 GATT 연결을 수립하고 연결 상태를 Flow로 방출한다.
     * GATT error 133 발생 시 최대 3회까지 재시도한다.
     */
    @SuppressLint("MissingPermission")
    fun connect(address: String): Flow<BleConnectionState> = callbackFlow {
        val device = bluetoothAdapter?.getRemoteDevice(address)
        if (device == null) {
            trySend(BleConnectionState.Error("Device not found", -1))
            close()
            return@callbackFlow
        }

        var retryCount = 0

        val gattCallback = object : BluetoothGattCallback() {
            override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
                when {
                    // GATT error 133 자동 재시도
                    status == GATT_ERROR_133 && retryCount < MAX_RETRY_COUNT -> {
                        retryCount++
                        Timber.d("GATT error 133 발생, 재시도 ($retryCount/$MAX_RETRY_COUNT)")
                        gatt.close()
                        gattRef = device.connectGatt(context, false, this, BluetoothDevice.TRANSPORT_LE)
                    }

                    newState == BluetoothProfile.STATE_CONNECTED && status == BluetoothGatt.GATT_SUCCESS -> {
                        Timber.d("GATT 연결 성공: $address")
                        retryCount = 0
                        trySend(BleConnectionState.DiscoveringServices)
                        gatt.discoverServices()
                    }

                    newState == BluetoothProfile.STATE_DISCONNECTED -> {
                        if (status != BluetoothGatt.GATT_SUCCESS) {
                            Timber.e("GATT 연결 해제 (status=$status)")
                            trySend(BleConnectionState.Error("Disconnected with error", status))
                        } else {
                            Timber.d("GATT 정상 연결 해제")
                            trySend(BleConnectionState.Idle)
                        }
                        cleanupPendingOperations()
                    }
                }
            }

            override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    Timber.d("GATT 서비스 발견 완료: ${gatt.services.size}개")
                    val services = gatt.services.map { it.toGattServiceInfo() }
                    trySend(BleConnectionState.Connected(services))
                } else {
                    Timber.e("GATT 서비스 발견 실패: status=$status")
                    trySend(BleConnectionState.Error("Service discovery failed", status))
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
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    Timber.d("Characteristic 쓰기 성공: ${characteristic.uuid}")
                    pendingWrite?.complete(Result.success(Unit))
                } else {
                    Timber.e("Characteristic 쓰기 실패: status=$status")
                    pendingWrite?.complete(Result.failure(Exception("Write failed with status: $status")))
                }
                pendingWrite = null
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
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    pendingDescriptorWrite?.complete(Result.success(Unit))
                } else {
                    pendingDescriptorWrite?.complete(
                        Result.failure(Exception("Descriptor write failed: $status")),
                    )
                }
                pendingDescriptorWrite = null
            }
        }

        trySend(BleConnectionState.Connecting)
        gattRef = device.connectGatt(context, false, gattCallback, BluetoothDevice.TRANSPORT_LE)

        // GATT 오퍼레이션 큐 처리 코루틴
        launch(ioDispatcher) {
            for (operation in operationQueue) {
                delay(OP_QUEUE_DELAY_MS)
                operation.execute()
            }
        }

        awaitClose {
            Timber.d("GATT 연결 정리")
            disconnect()
        }
    }

    @SuppressLint("MissingPermission")
    fun disconnect() {
        cleanupPendingOperations()
        gattRef?.let { gatt ->
            gatt.disconnect()
            gatt.close()
        }
        gattRef = null
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
            pendingRead = deferred

            operationQueue.trySend(GattOperation { gatt.readCharacteristic(characteristic) })

            withTimeoutOrNull(OP_TIMEOUT_MS) { deferred.await() }
                ?: Result.failure<ByteArray>(Exception("Read operation timed out")).also {
                    pendingRead = null
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
        pendingWrite = deferred

        operationQueue.trySend(
            GattOperation {
                gatt.writeCharacteristic(
                    characteristic,
                    value,
                    BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT,
                )
            },
        )

        withTimeoutOrNull(OP_TIMEOUT_MS) { deferred.await() }
            ?: Result.failure<Unit>(Exception("Write operation timed out")).also {
                pendingWrite = null
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
        pendingDescriptorWrite = deferred

        val descriptorValue = if (enable) {
            BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
        } else {
            BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE
        }

        operationQueue.trySend(
            GattOperation {
                gatt.setCharacteristicNotification(characteristic, enable)
                gatt.writeDescriptor(descriptor, descriptorValue)
            },
        )

        withTimeoutOrNull(OP_TIMEOUT_MS) { deferred.await() }
            ?: Result.failure<Unit>(Exception("Notification setup timed out")).also {
                pendingDescriptorWrite = null
            }
    }

    private fun findCharacteristic(
        gatt: BluetoothGatt,
        serviceUuid: String,
        charUuid: String,
    ): BluetoothGattCharacteristic? {
        val service = gatt.getService(UUID.fromString(serviceUuid))
        return service?.getCharacteristic(UUID.fromString(charUuid))
    }

    private fun handleCharacteristicRead(value: ByteArray?, status: Int) {
        if (status == BluetoothGatt.GATT_SUCCESS && value != null) {
            Timber.d("Characteristic 읽기 성공: ${value.size} bytes")
            pendingRead?.complete(Result.success(value))
        } else {
            Timber.e("Characteristic 읽기 실패: status=$status")
            pendingRead?.complete(Result.failure(Exception("Read failed with status: $status")))
        }
        pendingRead = null
    }

    private fun handleCharacteristicChanged(uuid: String, value: ByteArray?) {
        value?.let {
            Timber.d("Characteristic 변경 알림: $uuid, ${it.size} bytes")
            _characteristicUpdates.tryEmit(CharacteristicUpdate(uuid.lowercase(), it))
        }
    }

    private fun cleanupPendingOperations() {
        val cancelled = Exception("Operation cancelled: disconnected")
        pendingRead?.complete(Result.failure(cancelled))
        pendingRead = null
        pendingWrite?.complete(Result.failure(cancelled))
        pendingWrite = null
        pendingDescriptorWrite?.complete(Result.failure(cancelled))
        pendingDescriptorWrite = null
    }

    private fun interface GattOperation {
        fun execute()
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
