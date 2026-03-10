package com.jsh.wirelessexplorer.presentation.dashboard

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jsh.wirelessexplorer.R
import com.jsh.wirelessexplorer.presentation.theme.OnPrimary
import com.jsh.wirelessexplorer.presentation.theme.OnSurfaceMedium
import com.jsh.wirelessexplorer.presentation.theme.Primary
import com.jsh.wirelessexplorer.presentation.theme.SurfaceVariant

private val BUTTON_HEIGHT = 48.dp

@Composable
fun QuickActionRow(
    onNavigateToDevices: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val comingSoonText = stringResource(R.string.toast_coming_soon)

    Row(
        modifier = modifier.fillMaxWidth(),
    ) {
        // 스캔 시작 버튼
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

        // 속도 테스트 버튼 (미구현)
        OutlinedButton(
            onClick = {
                Toast.makeText(context, comingSoonText, Toast.LENGTH_SHORT).show()
            },
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, SurfaceVariant),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = OnSurfaceMedium,
            ),
            modifier = Modifier
                .weight(1f)
                .height(BUTTON_HEIGHT),
        ) {
            Text(
                text = stringResource(R.string.btn_speed_test),
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}
