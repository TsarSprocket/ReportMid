package com.tsarsprocket.reportmid.appBar.impl.view

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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

private const val BORDER_CUT_WIDTH = 4
private const val ICON_OFFSET = 24
private const val ICON_RADIUS = 16
private const val ICON_SIZE = 2 * ICON_RADIUS
private const val PADDING_START = 8
private const val PADDING_VERTICAL = 4

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

    Row {
        Spacer(Modifier.height(ICON_RADIUS.dp))

        SteppedRow(
            modifier = modifier
                .padding(start = PADDING_START.dp, top = PADDING_VERTICAL.dp, bottom = PADDING_VERTICAL.dp),
            step = ICON_OFFSET.dp,
        ) {
            for(index in urls.indices) {
                AsyncImage(
                    modifier = Modifier
                        .size(ICON_SIZE.dp)
                        .run {
                            if(index < urls.lastIndex) clip(crescentShape) else clip(fullMoonShape)
                        },
                    model = urls[index].iconUrl,
                    contentDescription = null,
                )
            }
        }
    }
}
