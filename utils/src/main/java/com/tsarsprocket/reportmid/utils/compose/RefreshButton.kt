package com.tsarsprocket.reportmid.utils.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tsarsprocket.reportmid.theme.reportMidColorScheme
import kotlin.math.sqrt

private const val MAX_ALPHA = 0.5f

@Composable
fun RefreshButton(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    untilEnabled: Float = 0f,
    onClick: () -> Unit,
) {
    val buttonColor = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
    val foregroundColor = if (enabled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary

    Box(
        modifier = modifier
            .size(48.dp)
            .alpha(MAX_ALPHA * sqrt(1f - untilEnabled))
            .clip(CircleShape)
            .background(buttonColor)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = "Refresh",
            modifier = Modifier.size(36.dp),
            tint = foregroundColor,
        )
        CircularProgressIndicator(
            progress = { untilEnabled },
            modifier = Modifier.size(45.dp),
            color = foregroundColor,
            trackColor = Color.Transparent,
        )
    }
}


@Preview
@Composable
private fun RefreshButtonPreview() {
    MaterialTheme(colorScheme = reportMidColorScheme) {
        RefreshButton(enabled = true, onClick = {})
    }
}


@Preview
@Composable
private fun RefreshButtonDisabledPreview() {
    MaterialTheme(colorScheme = reportMidColorScheme) {
        RefreshButton(enabled = false, untilEnabled = 0.25f, onClick = {})
    }
}
