package com.jsh.wirelessexplorer.presentation.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Nfc
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material.icons.outlined.Sensors
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jsh.wirelessexplorer.R
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jsh.wirelessexplorer.domain.model.DeviceHistory
import com.jsh.wirelessexplorer.domain.model.TechType
import com.jsh.wirelessexplorer.presentation.common.SignalBarsIndicator
import com.jsh.wirelessexplorer.presentation.history.FilterChipRow
import com.jsh.wirelessexplorer.presentation.theme.Background
import com.jsh.wirelessexplorer.presentation.theme.BleBlue
import com.jsh.wirelessexplorer.presentation.theme.NfcAmber
import com.jsh.wirelessexplorer.presentation.theme.OnSurface
import com.jsh.wirelessexplorer.presentation.theme.OnSurfaceDim
import com.jsh.wirelessexplorer.presentation.theme.Primary
import com.jsh.wirelessexplorer.presentation.theme.Surface
import com.jsh.wirelessexplorer.presentation.theme.SurfaceVariant
import com.jsh.wirelessexplorer.presentation.theme.UwbViolet
import com.jsh.wirelessexplorer.presentation.theme.WifiGreen
import androidx.compose.ui.tooling.preview.Preview
import com.jsh.wirelessexplorer.presentation.theme.WirelessExplorerTheme
import com.jsh.wirelessexplorer.presentation.util.RelativeTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val focusRequester = remember { FocusRequester() }

    // 화면 진입 시 자동 포커스
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Scaffold(
        modifier = modifier,
        containerColor = Background,
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_back),
                        )
                    }
                },
                title = {
                    SearchTextField(
                        query = uiState.query,
                        onQueryChange = viewModel::updateQuery,
                        onClear = { viewModel.updateQuery("") },
                        focusRequester = focusRequester,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Background,
                    navigationIconContentColor = OnSurface,
                ),
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            FilterChipRow(
                selectedFilter = uiState.selectedFilter,
                onFilterSelected = viewModel::selectFilter,
            )

            Spacer(modifier = Modifier.height(8.dp))

            when {
                // 검색어가 비어있으면 최근 검색어 표시
                !uiState.isSearching -> {
                    RecentSearchesSection(
                        recentSearches = uiState.recentSearches,
                        onSearchClick = viewModel::selectRecentSearch,
                        onDeleteClick = viewModel::deleteRecentSearch,
                        onClearAll = viewModel::clearRecentSearches,
                    )
                }

                // 검색 결과 없음
                uiState.isEmpty -> {
                    SearchEmptyState()
                }

                // 검색 결과 표시
                else -> {
                    SearchResultsList(
                        results = uiState.results,
                        query = uiState.query,
                        onResultClick = { device ->
                            viewModel.commitSearch()
                            onNavigateToDetail(device.id)
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchTextField(
    query: String,
    onQueryChange: (String) -> Unit,
    onClear: () -> Unit,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(SurfaceVariant)
            .padding(horizontal = 12.dp, vertical = 10.dp),
    ) {
        if (query.isEmpty()) {
            Text(
                text = stringResource(R.string.hint_search),
                color = OnSurfaceDim,
                fontSize = 15.sp,
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester),
                textStyle = TextStyle(
                    color = OnSurface,
                    fontSize = 15.sp,
                ),
                singleLine = true,
                cursorBrush = SolidColor(Primary),
            )
            if (query.isNotEmpty()) {
                IconButton(
                    onClick = onClear,
                    modifier = Modifier.size(24.dp),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(R.string.cd_clear),
                        tint = OnSurfaceDim,
                        modifier = Modifier.size(18.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun RecentSearchesSection(
    recentSearches: List<String>,
    onSearchClick: (String) -> Unit,
    onDeleteClick: (String) -> Unit,
    onClearAll: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (recentSearches.isEmpty()) return

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.label_recent_searches),
                color = OnSurfaceDim,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
            )
            TextButton(onClick = onClearAll) {
                Text(
                    text = stringResource(R.string.btn_clear_all),
                    color = Primary,
                    fontSize = 13.sp,
                )
            }
        }

        LazyColumn {
            items(recentSearches, key = { it }) { query ->
                RecentSearchItem(
                    query = query,
                    onClick = { onSearchClick(query) },
                    onDelete = { onDeleteClick(query) },
                )
            }
        }
    }
}

@Composable
private fun SearchResultsList(
    results: List<DeviceHistory>,
    query: String,
    onResultClick: (DeviceHistory) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = androidx.compose.foundation.layout.PaddingValues(
            horizontal = 16.dp,
            vertical = 4.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        items(results, key = { it.id }) { device ->
            SearchResultCard(
                device = device,
                query = query,
                onClick = { onResultClick(device) },
            )
        }
    }
}

@Composable
private fun SearchResultCard(
    device: DeviceHistory,
    query: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val (icon, tint) = device.techType.searchIconAndColor()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Surface)
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(tint.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = device.techType.label,
                tint = tint,
                modifier = Modifier.size(20.dp),
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            HighlightedText(
                text = device.deviceName,
                highlight = query,
                fontSize = 15.sp,
            )
            HighlightedText(
                text = device.deviceAddress,
                highlight = query,
                fontSize = 12.sp,
            )
            Text(
                text = RelativeTimeFormatter.formatRelativeTime(device.lastConnectedAt),
                color = OnSurfaceDim,
                fontSize = 11.sp,
            )
        }
        if (device.rssi != null) {
            SignalBarsIndicator(rssi = device.rssi)
        }
    }
}

@Composable
private fun SearchEmptyState(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = Icons.Outlined.SearchOff,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = OnSurfaceDim,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.empty_no_results),
            color = OnSurface,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.desc_try_scanning),
            color = OnSurfaceDim,
            fontSize = 14.sp,
        )
    }
}

private fun TechType.searchIconAndColor(): Pair<ImageVector, Color> = when (this) {
    TechType.BLE -> Icons.Filled.Bluetooth to BleBlue
    TechType.WIFI -> Icons.Filled.Wifi to WifiGreen
    TechType.NFC -> Icons.Filled.Nfc to NfcAmber
    TechType.UWB -> Icons.Outlined.Sensors to UwbViolet
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, backgroundColor = 0xFF0A0A1A)
@Composable
private fun SearchScreenPreview() {
    WirelessExplorerTheme {
        Scaffold(
            containerColor = Background,
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                            )
                        }
                    },
                    title = {
                        SearchTextField(
                            query = "Galaxy",
                            onQueryChange = {},
                            onClear = {},
                            focusRequester = remember { FocusRequester() },
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Background,
                        navigationIconContentColor = OnSurface,
                    ),
                )
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                SearchResultsList(
                    results = listOf(
                        DeviceHistory(
                            id = 1,
                            deviceName = "Galaxy Watch 5",
                            deviceAddress = "AA:BB:CC:DD:EE:01",
                            techType = TechType.BLE,
                            rssi = -45,
                        ),
                        DeviceHistory(
                            id = 2,
                            deviceName = "Galaxy Buds Pro",
                            deviceAddress = "AA:BB:CC:DD:EE:02",
                            techType = TechType.BLE,
                            rssi = -62,
                        ),
                    ),
                    query = "Galaxy",
                    onResultClick = {},
                )
            }
        }
    }
}
