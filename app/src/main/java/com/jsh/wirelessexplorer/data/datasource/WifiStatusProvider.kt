package com.jsh.wirelessexplorer.data.datasource

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import com.jsh.wirelessexplorer.di.IoDispatcher
import com.jsh.wirelessexplorer.domain.model.WifiStatus
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

// IP 주소 변환용 비트마스크/시프트
private const val IP_BYTE_MASK = 0xFF
private const val IP_SHIFT_SECOND_OCTET = 8
private const val IP_SHIFT_THIRD_OCTET = 16
private const val IP_SHIFT_FOURTH_OCTET = 24

@Singleton
class WifiStatusProvider @Inject constructor(
    @ApplicationContext private val context: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) {
    private val connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val wifiManager: WifiManager =
        context.getSystemService(Context.WIFI_SERVICE) as WifiManager

    /** 실시간 Wi-Fi 연결 상태를 관찰한다. */
    @SuppressLint("MissingPermission")
    fun observeWifiStatus(): Flow<WifiStatus?> = callbackFlow {
        val request = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onCapabilitiesChanged(
                network: Network,
                capabilities: NetworkCapabilities,
            ) {
                val status = extractWifiStatus(capabilities)
                trySend(status)
            }

            override fun onLost(network: Network) {
                Timber.d("Wi-Fi 네트워크 연결 해제됨")
                trySend(null)
            }
        }

        connectivityManager.registerNetworkCallback(request, callback)
        Timber.d("Wi-Fi 상태 콜백 등록")

        // 초기 상태 즉시 전송
        val initial = resolveCurrentStatus()
        trySend(initial)

        awaitClose {
            Timber.d("Wi-Fi 상태 콜백 해제")
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }.flowOn(ioDispatcher)

    /** 현재 활성 네트워크에서 Wi-Fi 상태를 조회한다. */
    @SuppressLint("MissingPermission")
    private fun resolveCurrentStatus(): WifiStatus? {
        val network = connectivityManager.activeNetwork ?: return null
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return null
        if (!capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) return null
        return extractWifiStatus(capabilities)
    }

    /** NetworkCapabilities에서 WifiStatus를 추출한다. */
    @SuppressLint("MissingPermission")
    private fun extractWifiStatus(capabilities: NetworkCapabilities): WifiStatus? {
        val wifiInfo = capabilities.transportInfo as? WifiInfo ?: return null

        val ssid = wifiInfo.ssid
            ?.removePrefix("\"")
            ?.removeSuffix("\"")
            ?.takeIf { it != "<unknown ssid>" }
            ?: return null

        val frequency = wifiInfo.frequency
        val channel = frequencyToChannel(frequency)
        val ipAddress = formatIpAddress(wifiInfo.ipAddress)
        val securityType = resolveSecurityType(wifiInfo.bssid)

        return WifiStatus(
            ssid = ssid,
            rssi = wifiInfo.rssi,
            linkSpeed = wifiInfo.linkSpeed,
            frequency = frequency,
            ipAddress = ipAddress,
            securityType = securityType,
            channel = channel,
            isConnected = true,
        )
    }

    /** 연결된 BSSID의 보안 유형을 스캔 결과에서 조회한다. */
    @SuppressLint("MissingPermission")
    private fun resolveSecurityType(bssid: String?): String {
        if (bssid == null) return "OPEN"
        val capabilities = wifiManager.scanResults
            .firstOrNull { it.BSSID == bssid }
            ?.capabilities ?: return "OPEN"
        return when {
            "WPA3" in capabilities -> "WPA3"
            "WPA2" in capabilities -> "WPA2"
            "WPA" in capabilities -> "WPA"
            "WEP" in capabilities -> "WEP"
            else -> "OPEN"
        }
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

    /** 정수형 IP 주소를 점 표기법 문자열로 변환한다. */
    private fun formatIpAddress(ip: Int): String =
        "${ip and IP_BYTE_MASK}.${ip shr IP_SHIFT_SECOND_OCTET and IP_BYTE_MASK}.${ip shr IP_SHIFT_THIRD_OCTET and IP_BYTE_MASK}.${ip shr IP_SHIFT_FOURTH_OCTET and IP_BYTE_MASK}"
}
