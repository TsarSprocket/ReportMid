package com.tsarsprocket.reportmid.appBar.impl.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.tsarsprocket.reportmid.appBar.impl.viewState.ShowingAppBarState
import com.tsarsprocket.reportmid.theme.ReportMidTheme
import kotlinx.collections.immutable.persistentListOf

private val ICON_SIZE = 20.dp
private const val VISIBLE_FRACTION = 0.25f
private const val OPACITY_STEP = 0.1f
private val PADDING_START = 8.dp
private val PADDING_VERTICAL = 4.dp

@Composable
internal fun AppBarIcons(modifier: Modifier = Modifier, state: ShowingAppBarState) {
    val urls = state.iconUrls
    val spacing = ICON_SIZE * VISIBLE_FRACTION
    val rowWidth = if (urls.isEmpty()) 0.dp else ICON_SIZE + spacing * urls.lastIndex

    Box(
        modifier = modifier
            .padding(start = PADDING_START, top = PADDING_VERTICAL, bottom = PADDING_VERTICAL)
            .height(ICON_SIZE)
            .width(rowWidth),
    ) {
        for (index in urls.indices.reversed()) {
            AppBarIcon(
                modifier = Modifier.offset(x = spacing * (urls.lastIndex - index)),
                url = urls[index],
                opacity = 1f - OPACITY_STEP * index,
                cutoutOffset = if (index > 0) spacing else null,
            )
        }
    }
}

@Composable
private fun AppBarIcon(modifier: Modifier, url: String, opacity: Float, cutoutOffset: Dp?) {
    val cutoutOffsetPx = cutoutOffset?.let { with(LocalDensity.current) { it.toPx() } }
    val shape = remember(cutoutOffsetPx) { appBarIconShape(cutoutOffsetPx) }

    AsyncImage(
        modifier = modifier
            .size(ICON_SIZE)
            .clip(shape)
            .alpha(opacity),
        model = url,
        contentDescription = null,
    )
}

private fun appBarIconShape(cutoutOffsetPx: Float?): Shape = GenericShape { size, _ ->
    if (cutoutOffsetPx == null) {
        addOval(Rect(Offset.Zero, size))
    } else {
        val main = Path().apply { addOval(Rect(Offset.Zero, size)) }
        val cutout = Path().apply { addOval(Rect(Offset(cutoutOffsetPx, 0f), size)) }
        op(main, cutout, PathOperation.Difference)
    }
}


@Preview
@Composable
private fun AppBarIconsPreview() {
    ReportMidTheme {
        AppBarIcons(
            state = ShowingAppBarState(
                iconUrls = persistentListOf(
                    "https://example.com/icon1.png",
                    "https://example.com/icon2.png",
                    "https://example.com/icon3.png",
                    "https://example.com/icon4.png",
                    "https://example.com/icon5.png",
                ),
            ),
        )
    }
}
