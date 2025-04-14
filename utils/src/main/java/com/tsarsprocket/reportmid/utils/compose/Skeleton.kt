package com.tsarsprocket.reportmid.utils.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tsarsprocket.reportmid.theme.ReportMidTheme
import com.tsarsprocket.reportmid.theme.reportMidColorScheme

@Composable
fun SkeletonRectangle(
    modifier: Modifier,
    cornerSize: Dp = 4.dp,
    borderWidth: Dp = 1.dp,
    borderColor: Color = reportMidColorScheme.onSurfaceVariant,
    backgroundColor: Color = reportMidColorScheme.surfaceVariant,
) {
    val shape = RoundedCornerShape(cornerSize)
    Box(
        modifier = modifier
            .background(color = backgroundColor, shape = shape)
            .border(width = borderWidth, color = borderColor, shape = shape)
    )
}


@Preview
@Composable
private fun SkeletonRectanglePreview() {
    ReportMidTheme {
        SkeletonRectangle(modifier = Modifier.size(width = 100.dp, height = 50.dp))
    }
}