package com.jsh.wirelessexplorer.feature.devices

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.jsh.wirelessexplorer.feature.devices.R
import com.jsh.wirelessexplorer.core.designsystem.theme.OnSurfaceDim
import com.jsh.wirelessexplorer.core.designsystem.theme.Primary
import com.jsh.wirelessexplorer.core.designsystem.theme.Surface
import com.jsh.wirelessexplorer.core.designsystem.theme.WirelessExplorerTheme
import com.jsh.wirelessexplorer.feature.ble.BleScreen
import com.jsh.wirelessexplorer.feature.nfc.NfcScreen
import com.jsh.wirelessexplorer.feature.uwb.UwbScreen
import com.jsh.wirelessexplorer.feature.wifi.WifiScreen
import kotlinx.coroutines.launch

private enum class DeviceTab(val labelResId: Int) {
    BLUETOOTH(R.string.tab_bluetooth),
    WIFI(R.string.tab_wifi),
    NFC(R.string.tab_nfc),
    UWB(R.string.tab_uwb),
}

@Composable
fun DevicesScreen(
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToBleDetail: (String) -> Unit = {},
    modifier: Modifier = Modifier,
    initialTab: Int = 0,
) {
    val tabs = DeviceTab.entries
    val pagerState = rememberPagerState(
        initialPage = initialTab.coerceIn(0, tabs.lastIndex),
        pageCount = { tabs.size },
    )
    val scope = rememberCoroutineScope()

    Column(modifier = modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = Surface,
            contentColor = Primary,
            indicator = { tabPositions ->
                SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                    color = Primary,
                )
            },
        ) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                    text = { Text(stringResource(tab.labelResId)) },
                    selectedContentColor = Primary,
                    unselectedContentColor = OnSurfaceDim,
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
        ) { page ->
            when (tabs[page]) {
                DeviceTab.BLUETOOTH -> BleScreen(
                    onNavigateToDetail = onNavigateToDetail,
                    onNavigateToBleDetail = onNavigateToBleDetail,
                )
                DeviceTab.WIFI -> WifiScreen(onNavigateToDetail = onNavigateToDetail)
                DeviceTab.NFC -> NfcScreen(onNavigateToDetail = onNavigateToDetail)
                DeviceTab.UWB -> UwbScreen(onNavigateToDetail = onNavigateToDetail)
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A1A)
@Composable
private fun DevicesScreenPreview() {
    WirelessExplorerTheme {
        DevicesScreen(
            onNavigateToDetail = {},
            initialTab = 0,
        )
    }
}
