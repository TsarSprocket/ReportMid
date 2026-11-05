package com.tsarsprocket.reportmid.utils.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

/**
 * A crescent moon shape for the `clip` modifier.
 *
 * The shape is the area inside the first circle and outside the second one. Both circles are vertically centered
 * in the clipped composable.
 *
 * @param radius radius of the first circle; nothing is painted outside of it. Its leftmost point is at the left edge
 * of the composable.
 * @param offset distance from the leftmost point of the first circle to the leftmost point of the second one.
 * A positive value shifts the second circle to the right.
 * @param eclipse radius of the second circle; nothing is painted inside of it.
 */
data class CrescentShape(
    val radius: Dp,
    val offset: Dp,
    val eclipse: Dp,
) : Shape {

    override fun createOutline(size: androidx.compose.ui.geometry.Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val radiusPx = with(density) { radius.toPx() }
        val offsetPx = with(density) { offset.toPx() }
        val eclipsePx = with(density) { eclipse.toPx() }
        val centerY = size.height / 2f

        val moon = Path().apply {
            addOval(Rect(center = Offset(x = radiusPx, y = centerY), radius = radiusPx))
        }
        val shadow = Path().apply {
            addOval(Rect(center = Offset(x = offsetPx + eclipsePx, y = centerY), radius = eclipsePx))
        }

        return Outline.Generic(Path.combine(PathOperation.Difference, moon, shadow))
    }
}


@Preview(showBackground = true)
@Composable
private fun CrescentShapePreview() {
    Box(
        modifier = Modifier
            .size(100.dp)
            .clip(CrescentShape(radius = 40.dp, offset = 20.dp, eclipse = 36.dp))
            .background(Color.Gray),
    )
}
