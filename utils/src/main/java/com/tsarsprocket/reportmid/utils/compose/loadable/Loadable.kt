package com.tsarsprocket.reportmid.utils.compose.loadable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier

/**
 * Visualizes a [LoadableHandle] by delegating to one of three content lambdas depending on the
 * current [LoadState].
 *
 * Multiple `Loadable` composables sharing the same [handle] all observe identical state — a single
 * load covers all of them. Composables that enter the composition late (during loading or after
 * success/failure) never restart the load; they simply render the current state.
 *
 * **Loading is triggered automatically** via `LaunchedEffect(handle)`, which calls
 * [LoadableHandle.start] once per handle instance. If the handle was already started (e.g. it is
 * hoisted in a ViewModel), the call is a no-op.
 *
 * **Reload is caller-driven.** Wire [LoadableHandle.reload] to any event source from outside this
 * composable — for example:
 * ```kotlin
 * // Click anywhere on the failure content
 * failure = { m, _ -> ErrorView(m.clickable { handle.reload() }) }
 *
 * // Separate retry button
 * Button(onClick = { handle.reload() }) { Text("Retry") }
 *
 * // Reactive trigger from external state
 * LaunchedEffect(shouldRetry) { if (shouldRetry) handle.reload() }
 * ```
 *
 * @param T The type of the successfully loaded data.
 * @param modifier Modifier applied to all content lambdas as their first argument.
 * @param handle Shared state holder that drives loading and exposes [LoadState].
 * @param loading Content shown while [LoadState.Loading] is active.
 * @param failure Content shown when [LoadState.Failed]. Receives the [Throwable] from the loader.
 * @param success Content shown when [LoadState.Loaded]. Receives the loaded [T].
 */
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
