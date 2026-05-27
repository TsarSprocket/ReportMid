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

/**
 * Shared state holder for the loadable mechanism. Multiple [Loadable] composables can reference
 * the same handle — they all observe the identical [state] and reflect any transition atomically.
 *
 * **Lifecycle.** The handle is decoupled from composition: pass any [CoroutineScope] whose lifetime
 * matches the desired load lifetime (e.g. `viewModelScope` for screen-level hoisting, or the scope
 * returned by [rememberCoroutineScope] for composition-local use). The scope also cancels any
 * in-flight load when it is cancelled.
 *
 * **Loading.** [start] kicks off the first load and is idempotent — subsequent calls while a job
 * is active or has completed are no-ops. [Loadable] calls [start] automatically via
 * `LaunchedEffect`, so normally you do not call it manually.
 *
 * **Reloading.** [reload] restarts loading from [LoadState.Failed]. It is a no-op when state is
 * [LoadState.Loaded] (already done — intentionally no re-fetch) or when a load is already in
 * flight (coalesces concurrent triggers). Call [reload] from any event source: `.clickable`,
 * `Button.onClick`, a `LaunchedEffect` watching external state, etc.
 *
 * @param T The type of the successfully loaded data.
 * @param scope Coroutine scope used to run the [loader].
 * @param loader Suspend function that performs the actual data fetch and returns [T].
 */
@Stable
class LoadableHandle<T>(
    private val scope: CoroutineScope,
    private val loader: suspend () -> T,
) {
    /** Current load state, backed by Compose snapshot state so composables recompose on change. */
    var state: LoadState<T> by mutableStateOf(LoadState.Loading)
        private set

    private var loadJob: Job? = null

    /**
     * Starts the initial load. Idempotent — does nothing if a load has already been launched.
     * Called automatically by [Loadable]; only call manually when you need to pre-warm the handle
     * before it is mounted in composition.
     */
    fun start() {
        if (loadJob == null) launchLoad()
    }

    /**
     * Retries loading after a failure. No-op when:
     * - state is [LoadState.Loaded] — successfully loaded data is never re-fetched.
     * - a load is already in flight — concurrent triggers are coalesced into the running job.
     */
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

/**
 * Creates and remembers a [LoadableHandle] whose coroutine scope is tied to the current
 * composition. The handle is re-created whenever [keys] change, which cancels any in-flight load
 * and restarts from scratch.
 *
 * For screen-level hoisting (e.g. sharing the handle across multiple destinations or surviving
 * configuration changes) construct [LoadableHandle] directly with `viewModelScope`.
 *
 * @param keys Values that identity the load. A change in any key recreates the handle and its load.
 * @param loader Suspend function that performs the actual data fetch.
 */
@Composable
fun <T> rememberLoadableHandle(
    vararg keys: Any?,
    loader: suspend () -> T,
): LoadableHandle<T> {
    val scope = rememberCoroutineScope()
    return remember(*keys) { LoadableHandle(scope, loader) }
}
