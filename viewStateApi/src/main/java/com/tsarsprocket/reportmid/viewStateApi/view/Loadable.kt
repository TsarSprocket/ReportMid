package com.tsarsprocket.reportmid.viewStateApi.view

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.tsarsprocket.reportmid.viewStateApi.viewState.LoadablePart


fun interface NotLoaded {
    @Composable
    operator fun invoke(modifier: Modifier)
}

fun interface Loading {
    @Composable
    operator fun invoke(modifier: Modifier)
}

fun interface Failure {
    @Composable
    operator fun invoke(modifier: Modifier, reloader: () -> Unit)
}

fun interface Success<T : Parcelable> {
    @Composable
    operator fun invoke(modifier: Modifier, data: T)
}

@Composable
fun <T : Parcelable> Loadable(
    modifier: Modifier = Modifier,
    part: LoadablePart<T>,
    notLoaded: NotLoaded? = null,
    loading: Loading,
    success: Success<T>,
    failure: Failure,
    loadTrigger: () -> Unit
) {
    LaunchedEffect(part) { if(part is LoadablePart.NotLoaded) loadTrigger() }

    when(part) {
        is LoadablePart.NotLoaded -> notLoaded?.let { it(modifier) } ?: loading(modifier)
        is LoadablePart.Loading -> loading(modifier)
        is LoadablePart.Loaded -> success(modifier, part.data)
        is LoadablePart.Failed -> failure(modifier, loadTrigger)
    }
}