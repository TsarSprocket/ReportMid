package com.tsarsprocket.reportmid.utils.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tsarsprocket.reportmid.theme.ReportMidTheme
import com.tsarsprocket.reportmid.theme.reportMidColorScheme
import com.tsarsprocket.reportmid.utils.R


@Composable
fun Failure(
    modifier: Modifier,
    backgroundColor: Color = reportMidColorScheme.surfaceVariant,
    iconPainter: Painter,
    iconColor: Color = reportMidColorScheme.onSurfaceVariant,
    iconSize: Dp,
    description: String?,
    onClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .background(color = backgroundColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        IconWithShadow(
            modifier = Modifier.size(iconSize),
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
            iconPainter = painterResource(id = R.drawable.ic_failure),
            iconSize = 80.dp,
            description = "Failure",
        )
    }
}