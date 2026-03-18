package com.jsh.wirelessexplorer.core.data.ble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.jsh.wirelessexplorer.core.model.BleDevice
import com.jsh.wirelessexplorer.core.common.TimeProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Singleton

/** UI 방출 주기 (ms) — 콜백마다 방출하지 않고 주기적으로 배치 방출 */
private const val EMIT_INTERVAL_MS = 500L

@Singleton
class BleScanner @Inject constructor(
    @ApplicationContext private val context: Context,
    private val bluetoothAdapter: BluetoothAdapter?,
    private val timeProvider: TimeProvider,
) {

    // 스캔 결과 캐시 — 상세 화면에서 최신 데이터 조회용
    private val latestResults = ConcurrentHashMap<String, BleDevice>()

    fun getDevice(address: String): BleDevice? = latestResults[address]

    fun isBluetoothEnabled(): Boolean = bluetoothAdapter?.isEnabled == true

    fun observeBluetoothEnabled(): Flow<Boolean> = callbackFlow {
        trySend(isBluetoothEnabled())

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(ctx: Context, intent: Intent) {
                if (intent.action != BluetoothAdapter.ACTION_STATE_CHANGED) return
                val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
                trySend(state == BluetoothAdapter.STATE_ON)
            }
        }
        context.registerReceiver(receiver, IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED))

        awaitClose { context.unregisterReceiver(receiver) }
    }

    /**
     * BLE 스캔을 시작하고 발견된 디바이스 목록을 방출한다.
     * 콜백은 ConcurrentHashMap에 축적만 하고, 500ms 주기로 배치 방출하여 성능 최적화.
     */
    @SuppressLint("MissingPermission")
    fun scanDevices(): Flow<List<BleDevice>> = callbackFlow {
        val scanner = bluetoothAdapter?.bluetoothLeScanner
        if (scanner == null) {
            Timber.e("BluetoothLeScanner 사용 불가")
            close(IllegalStateException("Bluetooth is not available"))
            return@callbackFlow
        }

        // Thread-safe device accumulator
        val discoveredDevices = ConcurrentHashMap<String, ScanResult>()
        val isDirty = AtomicBoolean(false)

        val scanCallback = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult) {
                discoveredDevices[result.device.address] = result
                isDirty.set(true)
            }

            override fun onBatchScanResults(results: MutableList<ScanResult>) {
                results.forEach { result ->
                    discoveredDevices[result.device.address] = result
                }
                isDirty.set(true)
            }

            override fun onScanFailed(errorCode: Int) {
                Timber.e("BLE 스캔 실패: errorCode=$errorCode")
                close(IllegalStateException("BLE scan failed with error code: $errorCode"))
            }
        }

        // 주기적 배치 방출 코루틴
        launch {
            while (true) {
                delay(EMIT_INTERVAL_MS)
                if (isDirty.compareAndSet(true, false)) {
                    val now = timeProvider.now()
                    val sorted = discoveredDevices.values
                        .sortedByDescending { it.rssi }
                        .map { it.toBleDevice(now) }
                    sorted.forEach { latestResults[it.address] = it }
                    trySend(sorted).also { result ->
                        if (result.isFailure) Timber.w("trySend 실패: 배치 방출")
                    }
                }
            }
        }

        val settings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .setReportDelay(0)
            .build()

        Timber.d("BLE 스캔 시작")
        scanner.startScan(null, settings, scanCallback)

        awaitClose {
            Timber.d("BLE 스캔 중지")
            scanner.stopScan(scanCallback)
            latestResults.clear()
        }
    }
}
