package com.jsh.wirelessexplorer.feature.history

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jsh.wirelessexplorer.core.model.TechType
import com.jsh.wirelessexplorer.core.ui.FilterChipRow as CoreFilterChipRow

@Composable
fun FilterChipRow(
    selectedFilter: TechType?,
    onFilterSelected: (TechType?) -> Unit,
    modifier: Modifier = Modifier,
) {
    CoreFilterChipRow(
        selectedFilter = selectedFilter,
        onFilterSelected = onFilterSelected,
        modifier = modifier,
    )
}
