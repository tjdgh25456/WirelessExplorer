package com.jsh.wirelessexplorer.core.data.dashboard

import android.net.TrafficStats
import com.jsh.wirelessexplorer.core.common.IoDispatcher
import com.jsh.wirelessexplorer.core.model.TrafficSpeed
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

private const val SAMPLE_INTERVAL_MS = 1_000L

@Singleton
class TrafficStatsProvider @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) {

    /** 1초 간격으로 실시간 네트워크 트래픽 속도를 방출한다. */
    fun observeTrafficSpeed(): Flow<TrafficSpeed> = flow {
        var prevRx = TrafficStats.getTotalRxBytes()
        var prevTx = TrafficStats.getTotalTxBytes()
        var prevTime = System.nanoTime()

        while (true) {
            delay(SAMPLE_INTERVAL_MS)

            val currentRx = TrafficStats.getTotalRxBytes()
            val currentTx = TrafficStats.getTotalTxBytes()
            val currentTime = System.nanoTime()

            val elapsedSec = (currentTime - prevTime) / 1_000_000_000.0
            if (elapsedSec > 0 && currentRx != TrafficStats.UNSUPPORTED.toLong()) {
                val downloadBps = ((currentRx - prevRx) / elapsedSec).toLong().coerceAtLeast(0)
                val uploadBps = ((currentTx - prevTx) / elapsedSec).toLong().coerceAtLeast(0)
                emit(TrafficSpeed(downloadBytesPerSec = downloadBps, uploadBytesPerSec = uploadBps))
            }

            prevRx = currentRx
            prevTx = currentTx
            prevTime = currentTime
        }
    }.flowOn(ioDispatcher)
}
