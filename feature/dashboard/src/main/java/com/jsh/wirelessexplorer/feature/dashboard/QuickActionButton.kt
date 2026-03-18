package com.jsh.wirelessexplorer.feature.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jsh.wirelessexplorer.feature.dashboard.R
import com.jsh.wirelessexplorer.core.designsystem.theme.OnPrimary
import com.jsh.wirelessexplorer.core.designsystem.theme.OnSurfaceMedium
import com.jsh.wirelessexplorer.core.designsystem.theme.Primary
import com.jsh.wirelessexplorer.core.designsystem.theme.SurfaceVariant
import com.jsh.wirelessexplorer.core.designsystem.theme.WifiGreen

private val BUTTON_HEIGHT = 48.dp

@Composable
fun QuickActionRow(
    onNavigateToDevices: () -> Unit,
    onNavigateToWifi: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
    ) {
        Button(
            onClick = onNavigateToDevices,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary,
                contentColor = OnPrimary,
            ),
            modifier = Modifier
                .weight(1f)
                .height(BUTTON_HEIGHT),
        ) {
            Text(
                text = stringResource(R.string.btn_start_scan),
                style = MaterialTheme.typography.labelLarge,
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        OutlinedButton(
            onClick = onNavigateToWifi,
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, WifiGreen.copy(alpha = 0.4f)),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = WifiGreen,
            ),
            modifier = Modifier
                .weight(1f)
                .height(BUTTON_HEIGHT),
        ) {
            Text(
                text = stringResource(R.string.btn_open_wifi),
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}
