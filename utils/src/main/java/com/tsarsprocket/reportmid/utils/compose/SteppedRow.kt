package com.tsarsprocket.reportmid.utils.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Lays out children from left to right, shifting each next child by [step] relative to the previous one:
 * the child number `i` (zero-based) starts at `i * step` along the X-axis. Children overlap when they are wider than
 * [step]; the later a child is, the higher it is in the Z order (the last child is drawn on top of all others).
 *
 * The width is the rightmost edge among the children, the height is the height of the tallest child.
 * Children are centered vertically.
 */
@Composable
fun SteppedRow(
    modifier: Modifier = Modifier,
    step: Dp,
    content: @Composable () -> Unit,
) {
    Layout(
        modifier = modifier,
        content = content,
    ) { measurables, constraints ->
        val stepPx = step.roundToPx()
        val childConstraints = constraints.copy(minWidth = 0, minHeight = 0)
        val placeables = measurables.map { it.measure(childConstraints) }

        val contentWidth = placeables.withIndex().maxOfOrNull { (index, placeable) -> index * stepPx + placeable.width } ?: 0
        val contentHeight = placeables.maxOfOrNull { it.height } ?: 0

        val width = contentWidth.coerceIn(constraints.minWidth, constraints.maxWidth)
        val height = contentHeight.coerceIn(constraints.minHeight, constraints.maxHeight)

        layout(width, height) {
            // Placement order defines the Z order: later placeables are drawn on top of earlier ones.
            placeables.forEachIndexed { index, placeable ->
                placeable.placeRelative(
                    x = index * stepPx,
                    y = (height - placeable.height) / 2,
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun SteppedRowPreview() {
    SteppedRow(step = 16.dp) {
        Box(Modifier
            .size(width = 48.dp, height = 32.dp)
            .background(Color.Red))
        Box(Modifier
            .size(width = 48.dp, height = 64.dp)
            .background(Color.Green))
        Box(Modifier
            .size(width = 48.dp, height = 48.dp)
            .background(Color.Blue))
        Box(Modifier
            .size(width = 48.dp, height = 24.dp)
            .background(Color.Yellow))
    }
}
