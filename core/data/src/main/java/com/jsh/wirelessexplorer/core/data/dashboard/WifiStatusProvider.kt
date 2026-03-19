package com.jsh.wirelessexplorer.core.data.dashboard

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import com.jsh.wirelessexplorer.core.common.IoDispatcher
import com.jsh.wirelessexplorer.core.common.formatIpAddress
import com.jsh.wirelessexplorer.core.common.frequencyToChannel
import com.jsh.wirelessexplorer.core.model.WifiSecurityType
import com.jsh.wirelessexplorer.core.model.WifiStatus
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

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
                trySend(status).also { result ->
                    if (result.isFailure) Timber.w("trySend 실패: Wi-Fi 상태 전송")
                }
            }

            override fun onLost(network: Network) {
                Timber.d("Wi-Fi 네트워크 연결 해제됨")
                trySend(null).also { result ->
                    if (result.isFailure) Timber.w("trySend 실패: Wi-Fi 연결 해제 전송")
                }
            }
        }

        // NetworkCallback 등록 (예외 시 Flow 종료)
        try {
            connectivityManager.registerNetworkCallback(request, callback)
            Timber.d("Wi-Fi 상태 콜백 등록")
        } catch (e: Exception) {
            Timber.e(e, "Wi-Fi 상태 콜백 등록 실패")
            close(e)
            return@callbackFlow
        }

        // 초기 상태 즉시 전송
        val initial = resolveCurrentStatus()
        trySend(initial)

        awaitClose {
            Timber.d("Wi-Fi 상태 콜백 해제")
            try {
                connectivityManager.unregisterNetworkCallback(callback)
            } catch (e: IllegalArgumentException) {
                Timber.w(e, "Wi-Fi 상태 콜백 이미 해제됨")
            }
        }
    }.distinctUntilChanged().flowOn(ioDispatcher)

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

        val bssid = wifiInfo.bssid ?: ""
        val frequency = wifiInfo.frequency
        val channel = frequencyToChannel(frequency)
        val ipAddress = formatIpAddress(wifiInfo.ipAddress)
        val securityType = resolveSecurityType(bssid)

        return WifiStatus(
            ssid = ssid,
            bssid = bssid,
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
    private fun resolveSecurityType(bssid: String): WifiSecurityType {
        if (bssid.isBlank()) return WifiSecurityType.OPEN
        val capabilities = (wifiManager.scanResults ?: emptyList())
            .firstOrNull { it.BSSID == bssid }
            ?.capabilities ?: return WifiSecurityType.OPEN
        return WifiSecurityType.from(capabilities)
    }

}
