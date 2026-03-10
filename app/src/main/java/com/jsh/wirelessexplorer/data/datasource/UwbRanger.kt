package com.jsh.wirelessexplorer.data.datasource

import android.content.Context
import android.content.pm.PackageManager
import com.jsh.wirelessexplorer.domain.model.UwbRangingResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

// 시뮬레이션 거리 파라미터 (미터)
private const val SIM_BASE_DISTANCE_MIN = 1.5f
private const val SIM_BASE_DISTANCE_RANDOM_RANGE = 2f
private const val SIM_DISTANCE_STEP = 0.15f
private const val SIM_DISTANCE_MIN = 0.1f
private const val SIM_DISTANCE_MAX = 8f

// 시뮬레이션 방향 파라미터 (도)
private const val SIM_AZIMUTH_HALF_RANGE = 180f
private const val SIM_AZIMUTH_FULL_RANGE = 360f
private const val SIM_AZIMUTH_MOVEMENT_SCALE = 2f
private const val SIM_AZIMUTH_NOISE_SCALE = 3f
private const val SIM_ELEVATION_RANDOM_RANGE = 30f
private const val SIM_ELEVATION_RANDOM_OFFSET = 15f
private const val SIM_ELEVATION_MOVEMENT_SCALE = 0.5f
private const val SIM_ELEVATION_MIN = -45f
private const val SIM_ELEVATION_MAX = 45f

// 시뮬레이션 시간 파라미터
private const val SIM_TIME_STEP = 0.1
private const val SIM_ELEVATION_FREQ_SCALE = 0.7
private const val SIM_EMIT_INTERVAL_MS = 100L

/**
 * UWB 레인징 데이터소스.
 *
 * 현재 시뮬레이션 모드로 동작한다. 실제 하드웨어 연동 시 androidx.core.uwb를
 * 사용하여 RangingParameters / RangingResult를 처리해야 한다.
 */
@Singleton
class UwbRanger @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    @Volatile
    private var isRanging = false

    fun isSupported(): Boolean =
        context.packageManager.hasSystemFeature(PackageManager.FEATURE_UWB)

    /**
     * UWB 레인징을 시작하고 결과를 주기적으로 방출한다.
     * 실제 구현 시 androidx.core.uwb.UwbManager를 통해 세션을 열어야 한다.
     */
    fun startRanging(): Flow<UwbRangingResult> = flow {
        isRanging = true
        Timber.d("UWB 레인징 시작 (시뮬레이션)")

        // 초기 시뮬레이션 파라미터
        var baseDistance = SIM_BASE_DISTANCE_MIN + Random.nextFloat() * SIM_BASE_DISTANCE_RANDOM_RANGE
        var baseAzimuth = Random.nextFloat() * SIM_AZIMUTH_FULL_RANGE - SIM_AZIMUTH_HALF_RANGE
        var baseElevation = Random.nextFloat() * SIM_ELEVATION_RANDOM_RANGE - SIM_ELEVATION_RANDOM_OFFSET
        var time = 0.0

        while (currentCoroutineContext().isActive && isRanging) {
            time += SIM_TIME_STEP

            // 자연스러운 거리 변화 시뮬레이션
            baseDistance += (Random.nextFloat() - 0.5f) * SIM_DISTANCE_STEP
            baseDistance = baseDistance.coerceIn(SIM_DISTANCE_MIN, SIM_DISTANCE_MAX)

            // 천천히 이동하는 상대 디바이스 시뮬레이션
            baseAzimuth += sin(time).toFloat() * SIM_AZIMUTH_MOVEMENT_SCALE + (Random.nextFloat() - 0.5f) * SIM_AZIMUTH_NOISE_SCALE
            baseAzimuth = ((baseAzimuth + SIM_AZIMUTH_HALF_RANGE) % SIM_AZIMUTH_FULL_RANGE) - SIM_AZIMUTH_HALF_RANGE

            baseElevation += cos(time * SIM_ELEVATION_FREQ_SCALE).toFloat() * SIM_ELEVATION_MOVEMENT_SCALE
            baseElevation = baseElevation.coerceIn(SIM_ELEVATION_MIN, SIM_ELEVATION_MAX)

            val result = UwbRangingResult(
                distanceMeters = baseDistance,
                azimuthDegrees = baseAzimuth,
                elevationDegrees = baseElevation,
            )
            emit(result)
            delay(SIM_EMIT_INTERVAL_MS)
        }

        Timber.d("UWB 레인징 중지")
    }

    fun stopRanging() {
        isRanging = false
    }
}
