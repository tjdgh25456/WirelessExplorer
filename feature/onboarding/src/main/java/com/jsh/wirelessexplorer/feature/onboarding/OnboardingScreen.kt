package com.jsh.wirelessexplorer.feature.onboarding

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jsh.wirelessexplorer.feature.onboarding.R
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.jsh.wirelessexplorer.core.designsystem.theme.Background
import com.jsh.wirelessexplorer.core.designsystem.theme.BleBlue
import com.jsh.wirelessexplorer.core.designsystem.theme.NfcAmber
import com.jsh.wirelessexplorer.core.designsystem.theme.OnPrimary
import com.jsh.wirelessexplorer.core.designsystem.theme.OnSurface
import com.jsh.wirelessexplorer.core.designsystem.theme.OnSurfaceDim
import com.jsh.wirelessexplorer.core.designsystem.theme.Primary
import com.jsh.wirelessexplorer.core.designsystem.theme.Surface
import com.jsh.wirelessexplorer.core.designsystem.theme.WifiGreen
import com.jsh.wirelessexplorer.core.designsystem.theme.WirelessExplorerTheme
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

private const val PAGE_COUNT = 3

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun OnboardingScreen(
    onOnboardingComplete: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState(pageCount = { PAGE_COUNT })

    // 페이지 변경 시 ViewModel 동기화
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .distinctUntilChanged()
            .collect { viewModel.setCurrentPage(it) }
    }

    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.NEARBY_WIFI_DEVICES,
            Manifest.permission.POST_NOTIFICATIONS,
        ),
    )

    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
        ) { page ->
            when (page) {
                0 -> WelcomePage(
                    onSkip = {
                        viewModel.completeOnboarding()
                        onOnboardingComplete()
                    },
                    onNext = {
                        scope.launch { pagerState.animateScrollToPage(1) }
                    },
                )
                1 -> PermissionsPage(
                    permissionItems = buildPermissionItems(permissionsState.permissions),
                    onRequestPermissions = { permissionsState.launchMultiplePermissionRequest() },
                    onNext = {
                        scope.launch { pagerState.animateScrollToPage(2) }
                    },
                )
                2 -> AllSetPage(
                    onGetStarted = {
                        viewModel.completeOnboarding()
                        onOnboardingComplete()
                    },
                )
            }
        }

        // 하단 페이지 인디케이터
        PageIndicator(
            pageCount = PAGE_COUNT,
            currentPage = pagerState.currentPage,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp),
        )
    }
}

@Composable
private fun WelcomePage(
    onSkip: () -> Unit,
    onNext: () -> Unit,
) {
    OnboardingPage(
        title = stringResource(R.string.onboarding_welcome_title),
        subtitle = stringResource(R.string.onboarding_welcome_subtitle),
        iconSlot = {
            GlowIcon(icon = Icons.Default.Sensors)
        },
        actionsSlot = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TextButton(onClick = onSkip) {
                    Text(text = stringResource(R.string.btn_skip), color = OnSurfaceDim, fontSize = 15.sp)
                }
                Button(
                    onClick = onNext,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Primary,
                        contentColor = OnPrimary,
                    ),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text(text = stringResource(R.string.btn_next))
                }
            }
        },
    )
}

@Composable
private fun PermissionsPage(
    permissionItems: List<PermissionItemData>,
    onRequestPermissions: () -> Unit,
    onNext: () -> Unit,
) {
    OnboardingPage(
        title = stringResource(R.string.onboarding_permissions_title),
        subtitle = stringResource(R.string.onboarding_permissions_subtitle),
        actionsSlot = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                permissionItems.forEach { item ->
                    PermissionRow(data = item)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onRequestPermissions,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Primary.copy(alpha = 0.15f),
                        contentColor = Primary,
                    ),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text(text = stringResource(R.string.btn_grant_permissions))
                }

                Button(
                    onClick = onNext,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Primary,
                        contentColor = OnPrimary,
                    ),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text(text = stringResource(R.string.btn_next))
                }
            }
        },
    )
}

@Composable
private fun AllSetPage(onGetStarted: () -> Unit) {
    OnboardingPage(
        title = stringResource(R.string.onboarding_allset_title),
        subtitle = stringResource(R.string.onboarding_allset_subtitle),
        iconSlot = {
            GlowIcon(icon = Icons.Default.CheckCircle)
        },
        actionsSlot = {
            Button(
                onClick = onGetStarted,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary,
                    contentColor = OnPrimary,
                ),
                shape = RoundedCornerShape(12.dp),
            ) {
                Text(
                    text = stringResource(R.string.btn_start_exploring),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(vertical = 4.dp),
                )
            }
        },
    )
}

/** Primary 글로우 배경을 가진 아이콘 */
@Composable
private fun GlowIcon(icon: ImageVector) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Primary.copy(alpha = 0.35f),
                        Primary.copy(alpha = 0.08f),
                        Color.Transparent,
                    ),
                ),
            ),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Primary,
            modifier = Modifier.size(56.dp),
        )
    }
}

private data class PermissionItemData(
    val icon: ImageVector,
    val title: String,
    val description: String,
    val granted: Boolean,
    val iconTint: Color,
)

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun buildPermissionItems(
    permissions: List<com.google.accompanist.permissions.PermissionState>,
): List<PermissionItemData> {
    // 권한 상태를 이름 기준으로 매핑
    fun isGranted(suffix: String): Boolean =
        permissions.find { it.permission.endsWith(suffix) }
            ?.status is PermissionStatus.Granted

    return listOf(
        PermissionItemData(
            icon = Icons.Default.Bluetooth,
            title = stringResource(R.string.permission_bluetooth),
            description = stringResource(R.string.permission_bluetooth_desc),
            granted = isGranted("BLUETOOTH_SCAN") && isGranted("BLUETOOTH_CONNECT"),
            iconTint = BleBlue,
        ),
        PermissionItemData(
            icon = Icons.Default.Wifi,
            title = stringResource(R.string.permission_wifi),
            description = stringResource(R.string.permission_wifi_desc),
            granted = isGranted("NEARBY_WIFI_DEVICES"),
            iconTint = WifiGreen,
        ),
        PermissionItemData(
            icon = Icons.Default.Notifications,
            title = stringResource(R.string.permission_notifications),
            description = stringResource(R.string.permission_notifications_desc),
            granted = isGranted("POST_NOTIFICATIONS"),
            iconTint = NfcAmber,
        ),
    )
}

@Composable
private fun PermissionRow(data: PermissionItemData) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Surface)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = data.icon,
            contentDescription = null,
            tint = data.iconTint,
            modifier = Modifier.size(24.dp),
        )

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = data.title,
                color = OnSurface,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
            )
            Text(
                text = data.description,
                color = OnSurfaceDim,
                fontSize = 12.sp,
            )
        }

        Icon(
            imageVector = if (data.granted) Icons.Default.CheckCircle else Icons.Default.Check,
            contentDescription = null,
            tint = if (data.granted) WifiGreen else OnSurfaceDim,
            modifier = Modifier.size(20.dp),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A1A)
@Composable
private fun OnboardingWelcomePreview() {
    WirelessExplorerTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Background),
        ) {
            WelcomePage(
                onSkip = {},
                onNext = {},
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A1A)
@Composable
private fun OnboardingAllSetPreview() {
    WirelessExplorerTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Background),
        ) {
            AllSetPage(onGetStarted = {})
        }
    }
}
