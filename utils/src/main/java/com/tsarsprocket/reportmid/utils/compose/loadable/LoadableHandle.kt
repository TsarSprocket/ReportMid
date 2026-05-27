package com.tsarsprocket.reportmid.utils.compose.loadable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Stable
class LoadableHandle<T>(
    private val scope: CoroutineScope,
    private val loader: suspend () -> T,
) {
    var state: LoadState<T> by mutableStateOf(LoadState.Loading)
        private set

    private var loadJob: Job? = null

    fun start() {
        if (loadJob == null) launchLoad()
    }

    fun reload() {
        val current = state
        if (current is LoadState.Loaded) return
        if (current is LoadState.Loading && loadJob?.isActive == true) return
        launchLoad()
    }

    private fun launchLoad() {
        loadJob?.cancel()
        state = LoadState.Loading
        loadJob = scope.launch {
            state = try {
                LoadState.Loaded(loader())
            } catch (t: CancellationException) {
                throw t
            } catch (t: Throwable) {
                LoadState.Failed(t)
            }
        }
    }
}

@Composable
fun <T> rememberLoadableHandle(
    vararg keys: Any?,
    loader: suspend () -> T,
): LoadableHandle<T> {
    val scope = rememberCoroutineScope()
    return remember(*keys) { LoadableHandle(scope, loader) }
}
