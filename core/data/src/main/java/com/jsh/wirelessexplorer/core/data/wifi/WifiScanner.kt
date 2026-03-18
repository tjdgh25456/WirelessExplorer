package com.jsh.wirelessexplorer.core.data.wifi

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import com.jsh.wirelessexplorer.core.common.IoDispatcher
import com.jsh.wirelessexplorer.core.model.WifiConnectionInfo
import com.jsh.wirelessexplorer.core.model.WifiNetwork
import com.jsh.wirelessexplorer.core.model.WifiSecurityType
import com.jsh.wirelessexplorer.core.model.WifiStandard
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

// Wi-Fi 2.4 GHz 대역 주파수 범위 (MHz)
private const val FREQ_2_4_GHZ_START = 2412
private const val FREQ_2_4_GHZ_END = 2484
private const val FREQ_2_4_GHZ_BASE = 2412
private const val FREQ_2_4_GHZ_CHANNEL_WIDTH = 5
private const val FREQ_2_4_GHZ_FIRST_CHANNEL = 1

// Wi-Fi 5 GHz 대역 주파수 범위 (MHz)
private const val FREQ_5_GHZ_START = 5170
private const val FREQ_5_GHZ_END = 5825
private const val FREQ_5_GHZ_BASE = 5170
private const val FREQ_5_GHZ_CHANNEL_WIDTH = 5
private const val FREQ_5_GHZ_FIRST_CHANNEL = 34

// Wi-Fi 6 GHz 대역 주파수 범위 (MHz)
private const val FREQ_6_GHZ_START = 5925
private const val FREQ_6_GHZ_BASE = 5955
private const val FREQ_6_GHZ_CHANNEL_WIDTH = 5
private const val FREQ_6_GHZ_FIRST_CHANNEL = 1

// ScanResult.CHANNEL_WIDTH_* 상수
private const val CHANNEL_WIDTH_20MHZ = 0
private const val CHANNEL_WIDTH_40MHZ = 1
private const val CHANNEL_WIDTH_80MHZ = 2
private const val CHANNEL_WIDTH_160MHZ = 3
private const val CHANNEL_WIDTH_80MHZ_PLUS_80MHZ = 4 // 80+80 MHz
private const val CHANNEL_WIDTH_320MHZ = 5 // Wi-Fi 7 (802.11be)

// 채널 대역폭 (MHz)
private const val BANDWIDTH_20 = 20
private const val BANDWIDTH_40 = 40
private const val BANDWIDTH_80 = 80
private const val BANDWIDTH_160 = 160
private const val BANDWIDTH_320 = 320

// IP 주소 변환용 비트마스크/시프트
private const val IP_BYTE_MASK = 0xFF
private const val IP_SHIFT_SECOND_OCTET = 8
private const val IP_SHIFT_THIRD_OCTET = 16
private const val IP_SHIFT_FOURTH_OCTET = 24

@Singleton
class WifiScanner @Inject constructor(
    @ApplicationContext private val context: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) {
    private val wifiManager: WifiManager =
        context.getSystemService(Context.WIFI_SERVICE) as WifiManager

    private val connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    /** 마지막 스캔 결과 캐시 (Android 스캔 제한: 2분에 4회) */
    @Volatile
    private var cachedNetworks: List<WifiNetwork> = emptyList()

    @SuppressLint("MissingPermission")
    fun scanNetworks(): Flow<List<WifiNetwork>> = callbackFlow {
        val connectedBssid = resolveConnectedBssid()

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(ctx: Context, intent: Intent) {
                val success = intent.getBooleanExtra(
                    WifiManager.EXTRA_RESULTS_UPDATED, false
                )
                val results = mapScanResults(connectedBssid)
                if (success) {
                    cachedNetworks = results
                    Timber.d("Wi-Fi 스캔 완료: %d개 네트워크 발견", results.size)
                } else {
                    Timber.d("Wi-Fi 스캔 쓰로틀링, 캐시된 결과 반환")
                }
                trySend(cachedNetworks).also { result ->
                    if (result.isFailure) Timber.w("trySend 실패: Wi-Fi 스캔 결과 전송")
                }
            }
        }

        // BroadcastReceiver 등록 (예외 시 Flow 종료)
        try {
            context.registerReceiver(receiver, IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
        } catch (e: Exception) {
            Timber.e(e, "Wi-Fi 스캔 리시버 등록 실패")
            close(e)
            return@callbackFlow
        }

        // 초기 캐시 결과 전송 후 새 스캔 요청
        if (cachedNetworks.isNotEmpty()) {
            trySend(cachedNetworks)
        }

        @Suppress("DEPRECATION")
        val started = wifiManager.startScan()
        if (!started) {
            Timber.w("Wi-Fi 스캔 시작 실패, 캐시된 결과 사용")
            trySend(mapScanResults(connectedBssid).also { cachedNetworks = it })
        }

        awaitClose {
            Timber.d("Wi-Fi 스캔 리시버 해제")
            try {
                context.unregisterReceiver(receiver)
            } catch (e: IllegalArgumentException) {
                Timber.w(e, "Wi-Fi 리시버 이미 해제됨")
            }
        }
    }.flowOn(ioDispatcher)

    /**
     * 현재 연결된 네트워크 정보를 실시간 관찰한다.
     * NetworkCallback을 통해 연결 상태 변경 시마다 새로운 값을 방출한다.
     */
    @SuppressLint("MissingPermission")
    fun observeConnectionInfo(): Flow<WifiConnectionInfo?> = callbackFlow {
        val request = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onCapabilitiesChanged(
                network: Network,
                capabilities: NetworkCapabilities,
            ) {
                trySend(buildConnectionInfo(capabilities))
            }

            override fun onLost(network: Network) {
                Timber.d("Wi-Fi 연결 해제됨")
                trySend(null)
            }
        }

        try {
            connectivityManager.registerNetworkCallback(request, callback)
        } catch (e: Exception) {
            Timber.e(e, "Wi-Fi 연결 정보 콜백 등록 실패")
            close(e)
            return@callbackFlow
        }

        // 초기값 즉시 방출
        trySend(getConnectionInfo())

        awaitClose {
            Timber.d("Wi-Fi 연결 정보 콜백 해제")
            try {
                connectivityManager.unregisterNetworkCallback(callback)
            } catch (e: IllegalArgumentException) {
                Timber.w(e, "Wi-Fi 연결 콜백 이미 해제됨")
            }
        }
    }.flowOn(ioDispatcher)

    /** 현재 연결된 네트워크 정보를 1회 조회한다. */
    @SuppressLint("MissingPermission")
    fun getConnectionInfo(): WifiConnectionInfo? {
        val network = connectivityManager.activeNetwork ?: return null
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return null
        if (!capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) return null
        return buildConnectionInfo(capabilities)
    }

    fun isWifiEnabled(): Boolean = wifiManager.isWifiEnabled

    fun observeWifiEnabled(): Flow<Boolean> = callbackFlow {
        trySend(isWifiEnabled())

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(ctx: Context, intent: Intent) {
                if (intent.action != WifiManager.WIFI_STATE_CHANGED_ACTION) return
                val state = intent.getIntExtra(
                    WifiManager.EXTRA_WIFI_STATE,
                    WifiManager.WIFI_STATE_UNKNOWN,
                )
                trySend(state == WifiManager.WIFI_STATE_ENABLED)
            }
        }
        context.registerReceiver(receiver, IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION))

        awaitClose {
            try {
                context.unregisterReceiver(receiver)
            } catch (e: IllegalArgumentException) {
                Timber.w(e, "Wi-Fi 상태 리시버 이미 해제됨")
            }
        }
    }.flowOn(ioDispatcher)

    // -- private helpers --

    /** NetworkCapabilities에서 WifiConnectionInfo를 추출한다. */
    @SuppressLint("MissingPermission")
    private fun buildConnectionInfo(capabilities: NetworkCapabilities): WifiConnectionInfo? {
        val wifiInfo = capabilities.transportInfo as? WifiInfo ?: return null
        val ssid = wifiInfo.ssid
            ?.removePrefix("\"")
            ?.removeSuffix("\"")
            ?.takeIf { it != "<unknown ssid>" }
            ?: return null

        val frequency = wifiInfo.frequency
        val channel = frequencyToChannel(frequency)
        val ipAddr = formatIpAddress(wifiInfo.ipAddress)
        val securityType = resolveConnectedSecurityType(wifiInfo.bssid)

        // 연결된 네트워크의 Wi-Fi 표준과 채널 폭 파악
        val scanResult = wifiInfo.bssid?.let { bssid ->
            (wifiManager.scanResults ?: emptyList()).firstOrNull { it.BSSID == bssid }
        }
        val wifiStandard = scanResult?.let {
            WifiStandard.from(it.wifiStandard, frequency)
        } ?: WifiStandard.UNKNOWN
        val channelWidthMhz = scanResult?.let { channelWidthToMhz(it.channelWidth) }

        return WifiConnectionInfo(
            ssid = ssid,
            bssid = wifiInfo.bssid ?: "",
            rssi = wifiInfo.rssi,
            linkSpeed = wifiInfo.linkSpeed,
            frequency = frequency,
            ipAddress = ipAddr,
            channel = channel,
            securityType = securityType,
            wifiStandard = wifiStandard,
            channelWidthMhz = channelWidthMhz,
        )
    }

    @SuppressLint("MissingPermission")
    private fun mapScanResults(connectedBssid: String?): List<WifiNetwork> =
        (wifiManager.scanResults ?: emptyList())
            .map { result ->
                WifiNetwork(
                    ssid = result.SSID.takeIf { it.isNotBlank() } ?: "(Hidden)",
                    bssid = result.BSSID,
                    rssi = result.level,
                    frequency = result.frequency,
                    securityType = WifiSecurityType.from(result.capabilities),
                    isConnected = result.BSSID == connectedBssid,
                    channelWidth = result.channelWidth,
                    channelWidthMhz = channelWidthToMhz(result.channelWidth),
                    wifiStandard = WifiStandard.from(result.wifiStandard, result.frequency),
                )
            }
            .sortedByDescending { it.rssi }

    @SuppressLint("MissingPermission")
    private fun resolveConnectedBssid(): String? {
        val network = connectivityManager.activeNetwork ?: return null
        val caps = connectivityManager.getNetworkCapabilities(network) ?: return null
        if (!caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) return null
        return (caps.transportInfo as? WifiInfo)?.bssid
    }

    /** 연결된 네트워크의 BSSID로 스캔 결과에서 보안 유형을 조회한다. */
    @SuppressLint("MissingPermission")
    private fun resolveConnectedSecurityType(bssid: String?): WifiSecurityType {
        if (bssid == null) return WifiSecurityType.OPEN
        val scanResult = (wifiManager.scanResults ?: emptyList()).firstOrNull { it.BSSID == bssid }
        return scanResult?.let { WifiSecurityType.from(it.capabilities) } ?: WifiSecurityType.OPEN
    }

    /** Wi-Fi 주파수(MHz)를 채널 번호로 변환한다. */
    private fun frequencyToChannel(freq: Int): Int = when {
        freq in FREQ_2_4_GHZ_START..FREQ_2_4_GHZ_END ->
            (freq - FREQ_2_4_GHZ_BASE) / FREQ_2_4_GHZ_CHANNEL_WIDTH + FREQ_2_4_GHZ_FIRST_CHANNEL
        freq in FREQ_5_GHZ_START..FREQ_5_GHZ_END ->
            (freq - FREQ_5_GHZ_BASE) / FREQ_5_GHZ_CHANNEL_WIDTH + FREQ_5_GHZ_FIRST_CHANNEL
        freq >= FREQ_6_GHZ_START ->
            (freq - FREQ_6_GHZ_BASE) / FREQ_6_GHZ_CHANNEL_WIDTH + FREQ_6_GHZ_FIRST_CHANNEL
        else -> 0
    }

    /**
     * ScanResult.channelWidth 상수를 MHz 값으로 변환한다.
     * CHANNEL_WIDTH_320MHZ(5)는 Wi-Fi 7 전용.
     */
    private fun channelWidthToMhz(channelWidth: Int): Int = when (channelWidth) {
        CHANNEL_WIDTH_20MHZ -> BANDWIDTH_20
        CHANNEL_WIDTH_40MHZ -> BANDWIDTH_40
        CHANNEL_WIDTH_80MHZ -> BANDWIDTH_80
        CHANNEL_WIDTH_160MHZ -> BANDWIDTH_160
        CHANNEL_WIDTH_80MHZ_PLUS_80MHZ -> BANDWIDTH_160 // 80+80 = 160 MHz 상당
        CHANNEL_WIDTH_320MHZ -> BANDWIDTH_320
        else -> BANDWIDTH_20
    }

    /** 정수형 IP 주소를 문자열로 변환한다. */
    private fun formatIpAddress(ip: Int): String =
        "${ip and IP_BYTE_MASK}.${ip shr IP_SHIFT_SECOND_OCTET and IP_BYTE_MASK}.${ip shr IP_SHIFT_THIRD_OCTET and IP_BYTE_MASK}.${ip shr IP_SHIFT_FOURTH_OCTET and IP_BYTE_MASK}"
}
