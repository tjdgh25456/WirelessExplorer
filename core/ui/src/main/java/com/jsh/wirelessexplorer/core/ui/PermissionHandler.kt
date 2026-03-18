package com.jsh.wirelessexplorer.core.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jsh.wirelessexplorer.core.ui.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.jsh.wirelessexplorer.core.designsystem.theme.OnPrimary
import com.jsh.wirelessexplorer.core.designsystem.theme.OnSurface
import com.jsh.wirelessexplorer.core.designsystem.theme.OnSurfaceDim
import com.jsh.wirelessexplorer.core.designsystem.theme.Primary
import com.jsh.wirelessexplorer.core.designsystem.theme.Surface

/**
 * 런타임 권한이 필요한 화면을 감싸는 공통 컴포저블.
 *
 * 모든 권한이 허용된 경우 [onPermissionsGranted] 콘텐츠를 표시하고,
 * 그렇지 않으면 권한 요청 UI를 표시한다.
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequirePermissions(
    permissions: List<String>,
    rationaleTitle: String,
    rationaleMessage: String,
    onPermissionsGranted: @Composable () -> Unit,
) {
    val permissionsState = rememberMultiplePermissionsState(permissions)

    // 모든 권한이 허용된 경우 본래 콘텐츠 표시
    if (permissionsState.permissions.all { it.status.isGranted }) {
        onPermissionsGranted()
        return
    }

    // 최초 진입 시 자동으로 권한 요청
    LaunchedEffect(Unit) {
        permissionsState.launchMultiplePermissionRequest()
    }

    // 권한이 거부된 상태 — 안내 화면 표시
    val context = LocalContext.current
    val shouldShowRationale = permissionsState.shouldShowRationale

    PermissionRationaleScreen(
        title = rationaleTitle,
        message = rationaleMessage,
        showSettingsButton = !shouldShowRationale,
        onRequestPermissions = { permissionsState.launchMultiplePermissionRequest() },
        onOpenSettings = { context.openAppSettings() },
    )
}

/**
 * 권한 거부 시 표시되는 안내 화면.
 */
@Composable
private fun PermissionRationaleScreen(
    title: String,
    message: String,
    showSettingsButton: Boolean,
    onRequestPermissions: () -> Unit,
    onOpenSettings: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = null,
            tint = OnSurfaceDim,
            modifier = Modifier.size(56.dp),
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = title,
            color = OnSurface,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = message,
            color = OnSurfaceDim,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp,
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 권한 요청 버튼
        Button(
            onClick = onRequestPermissions,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary,
                contentColor = OnPrimary,
            ),
        ) {
            Text(
                text = stringResource(R.string.btn_grant_permissions),
                fontWeight = FontWeight.SemiBold,
            )
        }

        // 영구 거부 상태일 때 설정 화면으로 이동하는 버튼
        if (showSettingsButton) {
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onOpenSettings,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Surface,
                    contentColor = Primary,
                ),
            ) {
                Text(
                    text = stringResource(R.string.btn_open_settings),
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

/**
 * 앱 상세 설정 화면을 연다.
 */
fun Context.openAppSettings() {
    startActivity(
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
        },
    )
}
