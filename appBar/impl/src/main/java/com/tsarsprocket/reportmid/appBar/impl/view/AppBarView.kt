package com.tsarsprocket.reportmid.appBar.impl.view

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.tsarsprocket.reportmid.appBar.impl.viewState.AppBarState
import com.tsarsprocket.reportmid.utils.compose.CrescentShape
import com.tsarsprocket.reportmid.utils.compose.SteppedRow

private const val BORDER_CUT_WIDTH = 2
private const val ICON_OFFSET = 16
private const val ICON_RADIUS = 10
private const val ICON_SIZE = 2 * ICON_RADIUS
private val PADDING_START = 8.dp
private val PADDING_VERTICAL = 4.dp

@Composable
internal fun AppBarView(modifier: Modifier = Modifier, state: AppBarState) {
    val urls = state.items.value

    val crescentShape = remember {
        CrescentShape(
            radius = ICON_RADIUS.dp,
            offset = (ICON_OFFSET - BORDER_CUT_WIDTH).dp,
            eclipse = (ICON_RADIUS + BORDER_CUT_WIDTH).dp,
        )
    }
    val fullMoonShape = remember {
        RoundedCornerShape(ICON_RADIUS.dp)
    }

    SteppedRow(
        modifier = modifier
            .padding(start = PADDING_START, top = PADDING_VERTICAL, bottom = PADDING_VERTICAL),
        step = ICON_OFFSET.dp,
    ) {
        for(index in urls.indices) {
            AsyncImage(
                modifier = Modifier
                    .size(ICON_SIZE.dp)
                    .run {
                        if(index < urls.lastIndex) clip(crescentShape) else clip(fullMoonShape)
                    },
                model = urls[index],
                contentDescription = null,
            )
        }
    }
}
