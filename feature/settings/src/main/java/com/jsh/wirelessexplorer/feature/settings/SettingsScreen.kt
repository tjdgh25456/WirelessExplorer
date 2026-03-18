package com.jsh.wirelessexplorer.feature.settings

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PhonelinkErase
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.WifiFind
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jsh.wirelessexplorer.feature.settings.R
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jsh.wirelessexplorer.core.model.AppTheme
import com.jsh.wirelessexplorer.core.model.SignalUnit
import com.jsh.wirelessexplorer.core.designsystem.theme.Background
import com.jsh.wirelessexplorer.core.designsystem.theme.Error
import com.jsh.wirelessexplorer.core.designsystem.theme.OnSurface
import com.jsh.wirelessexplorer.core.designsystem.theme.OnSurfaceDim
import com.jsh.wirelessexplorer.core.designsystem.theme.Primary
import com.jsh.wirelessexplorer.core.designsystem.theme.Surface
import com.jsh.wirelessexplorer.core.designsystem.theme.WirelessExplorerTheme
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.roundToInt

private data class ScanDurationOption(val seconds: Int, val labelResId: Int)

private val scanDurationOptions = listOf(
    ScanDurationOption(10, R.string.label_scan_10s),
    ScanDurationOption(30, R.string.label_scan_30s),
    ScanDurationOption(60, R.string.label_scan_60s),
    ScanDurationOption(0, R.string.label_scan_unlimited),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val settings = state.settings
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val versionCopiedMessage = stringResource(R.string.toast_version_copied)

    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json"),
    ) { uri ->
        uri ?: return@rememberLauncherForActivityResult
        scope.launch {
            runCatching {
                val json = viewModel.exportHistoryAsJson()
                context.contentResolver.openOutputStream(uri)?.use { it.write(json.toByteArray()) }
            }.onFailure { e -> Timber.e(e, "JSON 내보내기 실패") }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.title_settings),
                        fontWeight = FontWeight.Bold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_back),
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Background,
                    titleContentColor = OnSurface,
                    navigationIconContentColor = OnSurface,
                ),
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Background,
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            // -- SCANNING --
            item { SectionHeader(title = stringResource(R.string.section_scanning)) }

            item {
                SettingDropdownItem(
                    icon = Icons.Default.Timer,
                    title = stringResource(R.string.setting_scan_duration),
                    selectedValue = scanDurationOptions.find { it.seconds == settings.scanDuration }
                        ?: scanDurationOptions[1],
                    options = scanDurationOptions,
                    displayText = { stringResource(it.labelResId) },
                    onOptionSelected = { viewModel.updateScanDuration(it.seconds) },
                )
            }

            item {
                SettingToggleItem(
                    icon = Icons.Default.WifiFind,
                    title = stringResource(R.string.setting_auto_scan),
                    subtitle = stringResource(R.string.setting_auto_scan_desc),
                    checked = settings.autoScanOnOpen,
                    onCheckedChange = viewModel::updateAutoScan,
                )
            }

            item {
                SettingToggleItem(
                    icon = Icons.Default.Visibility,
                    title = stringResource(R.string.setting_show_unnamed),
                    subtitle = stringResource(R.string.setting_show_unnamed_desc),
                    checked = settings.showUnnamedDevices,
                    onCheckedChange = viewModel::updateShowUnnamed,
                )
            }

            item {
                SettingToggleItem(
                    icon = Icons.Default.FilterAlt,
                    title = stringResource(R.string.setting_filter_weak),
                    subtitle = stringResource(R.string.setting_filter_weak_desc),
                    checked = settings.filterWeakSignals,
                    onCheckedChange = viewModel::updateFilterWeakSignals,
                )
            }

            item {
                // RSSI 임계값 슬라이더: 실시간 UI 반영 후 손을 뗄 때 저장
                var sliderValue by remember(settings.rssiThreshold) {
                    mutableFloatStateOf(settings.rssiThreshold.toFloat())
                }
                SettingSliderItem(
                    title = stringResource(R.string.setting_rssi_threshold),
                    value = sliderValue,
                    valueRange = -100f..-30f,
                    valueLabel = "${sliderValue.roundToInt()} dBm",
                    onValueChange = { sliderValue = it },
                    onValueChangeFinished = {
                        viewModel.updateRssiThreshold(sliderValue.roundToInt())
                    },
                )
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

            // -- DISPLAY --
            item { SectionHeader(title = stringResource(R.string.section_display)) }

            item {
                SettingChipsItem(
                    title = stringResource(R.string.setting_app_theme),
                    options = AppTheme.entries,
                    selectedOption = settings.appTheme,
                    displayText = { it.label },
                    onOptionSelected = viewModel::updateAppTheme,
                )
            }

            item {
                SettingChipsItem(
                    title = stringResource(R.string.setting_signal_unit),
                    options = SignalUnit.entries,
                    selectedOption = settings.signalUnit,
                    displayText = { it.label },
                    onOptionSelected = viewModel::updateSignalUnit,
                )
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

            // -- NOTIFICATIONS --
            item { SectionHeader(title = stringResource(R.string.section_notifications)) }

            item {
                SettingToggleItem(
                    icon = Icons.Default.NotificationsActive,
                    title = stringResource(R.string.setting_new_device_alerts),
                    subtitle = stringResource(R.string.setting_new_device_alerts_desc),
                    checked = settings.newDeviceAlerts,
                    onCheckedChange = viewModel::updateNewDeviceAlerts,
                )
            }

            item {
                SettingToggleItem(
                    icon = Icons.Default.PhonelinkErase,
                    title = stringResource(R.string.setting_connection_lost),
                    subtitle = stringResource(R.string.setting_connection_lost_desc),
                    checked = settings.connectionLostAlert,
                    onCheckedChange = viewModel::updateConnectionLostAlert,
                )
            }

            item {
                SettingToggleItem(
                    icon = Icons.Default.SignalCellularAlt,
                    title = stringResource(R.string.setting_signal_strength),
                    subtitle = stringResource(R.string.setting_signal_strength_desc),
                    checked = settings.signalStrengthAlert,
                    onCheckedChange = viewModel::updateSignalStrengthAlert,
                )
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

            // -- ABOUT --
            item { SectionHeader(title = stringResource(R.string.section_about)) }

            item {
                SettingClickableItem(
                    icon = Icons.Default.Info,
                    title = stringResource(R.string.setting_version),
                    subtitle = state.appVersion,
                    onClick = {
                        clipboardManager.setText(AnnotatedString(state.appVersion))
                        scope.launch { snackbarHostState.showSnackbar(versionCopiedMessage) }
                    },
                )
            }

            item {
                SettingClickableItem(
                    icon = Icons.Default.Upload,
                    title = stringResource(R.string.setting_export_data),
                    subtitle = stringResource(R.string.setting_export_data_desc),
                    onClick = { exportLauncher.launch("wireless_explorer_history.json") },
                )
            }

            item { HorizontalDivider(color = Surface, thickness = 1.dp) }

            item {
                SettingClickableItem(
                    icon = Icons.Default.DeleteForever,
                    title = stringResource(R.string.setting_clear_history),
                    subtitle = stringResource(R.string.setting_clear_history_desc),
                    onClick = viewModel::showClearHistoryDialog,
                    titleColor = Error,
                )
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }

    // 전체 이력 삭제 확인 다이얼로그
    if (state.showClearHistoryDialog) {
        AlertDialog(
            onDismissRequest = viewModel::dismissClearHistoryDialog,
            title = { Text(stringResource(R.string.dialog_clear_history_title), color = OnSurface) },
            text = {
                Text(
                    stringResource(R.string.dialog_clear_history_message),
                    color = OnSurfaceDim,
                )
            },
            confirmButton = {
                TextButton(onClick = viewModel::clearAllHistory) {
                    Text(stringResource(R.string.btn_delete_all), color = Error)
                }
            },
            dismissButton = {
                TextButton(onClick = viewModel::dismissClearHistoryDialog) {
                    Text(stringResource(R.string.btn_cancel), color = OnSurfaceDim)
                }
            },
            containerColor = Surface,
        )
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        color = Primary,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.2.sp,
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 4.dp),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, backgroundColor = 0xFF0A0A1A)
@Composable
private fun SettingsScreenPreview() {
    WirelessExplorerTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Settings",
                            fontWeight = FontWeight.Bold,
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Background,
                        titleContentColor = OnSurface,
                        navigationIconContentColor = OnSurface,
                    ),
                )
            },
            containerColor = Background,
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                item { SectionHeader(title = "SCANNING") }
                item { SectionHeader(title = "DISPLAY") }
                item { SectionHeader(title = "NOTIFICATIONS") }
                item { SectionHeader(title = "ABOUT") }
                item { Spacer(modifier = Modifier.height(32.dp)) }
            }
        }
    }
}
