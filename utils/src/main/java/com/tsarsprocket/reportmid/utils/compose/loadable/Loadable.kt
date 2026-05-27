package com.tsarsprocket.reportmid.utils.compose.loadable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier

@Composable
fun <T> Loadable(
    modifier: Modifier = Modifier,
    handle: LoadableHandle<T>,
    loading: @Composable (modifier: Modifier) -> Unit,
    failure: @Composable (modifier: Modifier, throwable: Throwable) -> Unit,
    success: @Composable (modifier: Modifier, data: T) -> Unit,
) {
    LaunchedEffect(handle) { handle.start() }

    when (val state = handle.state) {
        is LoadState.Loading -> loading(modifier)
        is LoadState.Failed -> failure(modifier, state.throwable)
        is LoadState.Loaded -> success(modifier, state.data)
    }
}
