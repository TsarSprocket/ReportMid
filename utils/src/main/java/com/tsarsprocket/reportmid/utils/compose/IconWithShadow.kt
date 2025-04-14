package com.tsarsprocket.reportmid.utils.compose

import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tsarsprocket.reportmid.theme.ReportMidTheme

@Composable
fun IconWithShadow(
    modifier: Modifier = Modifier,
    iconPainter: Painter,
    iconColor: Color = LocalContentColor.current,
    shadowColor: Color = Color.Black,
    shadowHorizontalOffset: Dp = 2.dp,
    shadowVerticalOffset: Dp = 2.dp,
    blurRadius: Dp = 4.dp,
    contentDescription: String? = null,
) {
    Icon(
        modifier = modifier
            .offset(x = shadowHorizontalOffset, y = shadowVerticalOffset)
            .blur(radius = blurRadius, edgeTreatment = BlurredEdgeTreatment.Unbounded),
        painter = iconPainter,
        tint = shadowColor,
        contentDescription = null,
    )

    Icon(
        modifier = modifier,
        painter = iconPainter,
        tint = iconColor,
        contentDescription = contentDescription,
    )
}


@Preview
@Composable
private fun IconWithShadowPreview() {
    val painter = object : Painter() {
        override val intrinsicSize = Size(100f, 100f)

        override fun DrawScope.onDraw() {
            drawCircle(
                color = Color.White,
                radius = size.width / 3,
                center = center,
                style = Stroke(width = 2f)
            )
        }
    }

    ReportMidTheme {
        IconWithShadow(
            iconPainter = painter,
            iconColor = Color.Gray,
            contentDescription = "Failure icon",
        )
    }
}