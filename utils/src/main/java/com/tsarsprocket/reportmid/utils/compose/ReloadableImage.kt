package com.tsarsprocket.reportmid.utils.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImagePainter.State
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageScope
import coil.request.ImageRequest


private const val RETRY_ATTEMPT = "retry_attempt"

@Composable
fun ReloadableImage(
    url: String,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    loading: @Composable (SubcomposeAsyncImageScope.(Modifier, State.Loading) -> Unit),
    error: @Composable (SubcomposeAsyncImageScope.(Modifier, State.Error, () -> Unit) -> Unit),
) {
    var retryCount by remember { mutableIntStateOf(0) }

    SubcomposeAsyncImage(
        modifier = modifier,
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .setParameter(RETRY_ATTEMPT, retryCount)
            .build(),
        contentDescription = contentDescription,
        loading = { state ->
            loading(modifier, state)
        },
        error = { state ->
            error(modifier, state) {
                ++retryCount
            }
        },
    )
}
