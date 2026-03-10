package com.jsh.wirelessexplorer.data.datasource

import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import com.jsh.wirelessexplorer.data.mapper.toBleDevice
import com.jsh.wirelessexplorer.domain.model.BleDevice
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BleScanner @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    private val bluetoothManager by lazy {
        context.getSystemService(BluetoothManager::class.java)
    }

    private val bluetoothAdapter by lazy { bluetoothManager.adapter }

    fun isBluetoothEnabled(): Boolean = bluetoothAdapter?.isEnabled == true

    /**
     * BLE 스캔을 시작하고 발견된 디바이스 목록을 방출한다.
     * RSSI 기준 내림차순 정렬, MAC 주소 기준 중복 제거.
     */
    @SuppressLint("MissingPermission")
    fun scanDevices(): Flow<List<BleDevice>> = callbackFlow {
        val scanner = bluetoothAdapter?.bluetoothLeScanner
        if (scanner == null) {
            Timber.e("BluetoothLeScanner 사용 불가")
            close(IllegalStateException("Bluetooth is not available"))
            return@callbackFlow
        }

        // Thread-safe device accumulator (ConcurrentHashMap으로 동시 접근 안전 보장)
        val discoveredDevices = ConcurrentHashMap<String, ScanResult>()

        val scanCallback = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult) {
                val address = result.device.address
                discoveredDevices[address] = result

                // RSSI 내림차순 정렬 후 방출
                val sorted = discoveredDevices.values
                    .sortedByDescending { it.rssi }
                    .map { it.toBleDevice() }
                val sendResult = trySend(sorted)
                if (sendResult.isFailure) {
                    Timber.w("trySend 실패: onScanResult")
                }
            }

            override fun onBatchScanResults(results: MutableList<ScanResult>) {
                results.forEach { result ->
                    discoveredDevices[result.device.address] = result
                }
                val sorted = discoveredDevices.values
                    .sortedByDescending { it.rssi }
                    .map { it.toBleDevice() }
                val sendResult = trySend(sorted)
                if (sendResult.isFailure) {
                    Timber.w("trySend 실패: onBatchScanResults")
                }
            }

            override fun onScanFailed(errorCode: Int) {
                Timber.e("BLE 스캔 실패: errorCode=$errorCode")
                close(IllegalStateException("BLE scan failed with error code: $errorCode"))
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
        }
    }
}
