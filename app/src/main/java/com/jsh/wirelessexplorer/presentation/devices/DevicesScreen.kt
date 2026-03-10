package com.jsh.wirelessexplorer.presentation.devices

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import com.jsh.wirelessexplorer.R
import com.jsh.wirelessexplorer.presentation.devices.ble.BleScreen
import com.jsh.wirelessexplorer.presentation.devices.nfc.NfcScreen
import com.jsh.wirelessexplorer.presentation.devices.uwb.UwbScreen
import com.jsh.wirelessexplorer.presentation.devices.wifi.WifiScreen
import androidx.compose.ui.tooling.preview.Preview
import com.jsh.wirelessexplorer.presentation.theme.Background
import com.jsh.wirelessexplorer.presentation.theme.OnSurfaceDim
import com.jsh.wirelessexplorer.presentation.theme.Primary
import com.jsh.wirelessexplorer.presentation.theme.Surface
import com.jsh.wirelessexplorer.presentation.theme.WirelessExplorerTheme
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
    modifier: Modifier = Modifier,
) {
    val tabs = DeviceTab.entries
    val pagerState = rememberPagerState(pageCount = { tabs.size })
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
                DeviceTab.BLUETOOTH -> BleScreen()
                DeviceTab.WIFI -> WifiScreen()
                DeviceTab.NFC -> NfcScreen()
                DeviceTab.UWB -> UwbScreen()
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
        )
    }
}
