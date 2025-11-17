package com.tsarsprocket.reportmid.utils.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.tsarsprocket.reportmid.theme.ReportMidTheme
import com.tsarsprocket.reportmid.theme.reportMidColorScheme
import com.tsarsprocket.reportmid.resLib.R as ResLibR

const val DEFAULT_FAILURE_ICON_SCALE = 0.66f

@Composable
fun Failure(
    modifier: Modifier,
    iconPainter: Painter,
    backgroundColor: Color = reportMidColorScheme.surfaceVariant,
    description: String? = null,
    iconColor: Color = reportMidColorScheme.onSurfaceVariant,
    iconScale: Float = DEFAULT_FAILURE_ICON_SCALE,
    onClick: () -> Unit = {},
) {
    BoxWithConstraints(
        modifier = modifier
            .background(color = backgroundColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        val size = min(maxHeight, maxWidth)

        IconWithShadow(
            modifier = Modifier.size(size * iconScale),
            iconPainter = iconPainter,
            iconColor = iconColor,
            contentDescription = description,
        )
    }
}


@Preview
@Composable
private fun FailurePreview() {
    ReportMidTheme {
        Failure(
            modifier = Modifier.size(120.dp),
            iconPainter = painterResource(id = ResLibR.drawable.ic_failure),
            description = "Failure",
        )
    }
}