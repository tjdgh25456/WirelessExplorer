package com.jsh.wirelessexplorer.presentation.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jsh.wirelessexplorer.R
import com.jsh.wirelessexplorer.presentation.theme.OnSurfaceDim
import com.jsh.wirelessexplorer.presentation.theme.OnSurfaceMedium
import com.jsh.wirelessexplorer.presentation.theme.SurfaceVariant
import com.jsh.wirelessexplorer.presentation.theme.WirelessExplorerTheme
import androidx.compose.ui.tooling.preview.Preview

private val GAUGE_PLACEHOLDER_SIZE = 120.dp

@Composable
fun NotConnectedCard(
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = com.jsh.wirelessexplorer.presentation.theme.Surface,
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 48.dp, horizontal = 20.dp),
        ) {
            Icon(
                imageVector = Icons.Rounded.WifiOff,
                contentDescription = stringResource(R.string.cd_not_connected),
                tint = OnSurfaceDim,
                modifier = Modifier.size(56.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.label_not_connected),
                style = MaterialTheme.typography.titleLarge,
                color = OnSurfaceMedium,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.desc_not_connected),
                style = MaterialTheme.typography.bodyMedium,
                color = OnSurfaceDim,
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 빈 게이지 자리 표시
            Surface(
                shape = RoundedCornerShape(50),
                color = SurfaceVariant,
                modifier = Modifier.size(GAUGE_PLACEHOLDER_SIZE),
            ) {}
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A1A)
@Composable
private fun NotConnectedCardPreview() {
    WirelessExplorerTheme {
        NotConnectedCard()
    }
}
