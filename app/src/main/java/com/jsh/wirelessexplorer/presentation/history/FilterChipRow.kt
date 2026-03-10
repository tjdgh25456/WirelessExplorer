package com.jsh.wirelessexplorer.presentation.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jsh.wirelessexplorer.R
import com.jsh.wirelessexplorer.domain.model.TechType
import com.jsh.wirelessexplorer.presentation.theme.OnPrimary
import com.jsh.wirelessexplorer.presentation.theme.OnSurfaceDim
import com.jsh.wirelessexplorer.presentation.theme.Primary
import com.jsh.wirelessexplorer.presentation.theme.SurfaceVariant

private data class FilterOption(
    val labelResId: Int,
    val techType: TechType?,
)

private val filterOptions = listOf(
    FilterOption(R.string.filter_all, null),
    FilterOption(R.string.filter_ble, TechType.BLE),
    FilterOption(R.string.filter_wifi, TechType.WIFI),
    FilterOption(R.string.filter_nfc, TechType.NFC),
    FilterOption(R.string.filter_uwb, TechType.UWB),
)

@Composable
fun FilterChipRow(
    selectedFilter: TechType?,
    onFilterSelected: (TechType?) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(filterOptions, key = { it.labelResId }) { option ->
            val selected = option.techType == selectedFilter
            FilterChip(
                selected = selected,
                onClick = { onFilterSelected(option.techType) },
                label = { Text(stringResource(option.labelResId)) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Primary,
                    selectedLabelColor = OnPrimary,
                    containerColor = SurfaceVariant,
                    labelColor = OnSurfaceDim,
                ),
                border = FilterChipDefaults.filterChipBorder(
                    borderColor = SurfaceVariant,
                    selectedBorderColor = Primary,
                    enabled = true,
                    selected = selected,
                ),
            )
        }
    }
}
