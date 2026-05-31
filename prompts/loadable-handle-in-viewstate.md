# Making `LoadableHandle` part of `ViewState`

## Goal
Hoist `LoadableHandle` instances into `ViewState` instances so loading state is owned by
the holder (survives config change / process death) rather than by composition.

## Blockers
1. Neither `LoadableHandle` nor `LoadState` is `Parcelable`.
2. `LoadableHandle` holds a `CoroutineScope` field + a `loader` lambda (neither parcelable).
3. (Not originally noted) **Dependencies**: the `loader` lambda *captures* repositories from
   DI. An abstract `load()` can't capture anything, and a `Parcelable` can't hold injected
   deps (they'd be null after restore).

## The existing template
`InternalSummonerViewState` (summonerViewImpl/.../viewState/InternalSummonerViewState.kt:31)
is the proven pattern for a stateful `ViewState`:
- holds only **Parcelable identity data** + sub-`ViewStateHolder`s,
- gets its live scope **from outside** via `setParentHolder(holder)` -> `holder.viewHolderScope`,
- (re)starts work in `start()`, which is called again after a parcel restore.

It never holds a scope or a fetch lambda in its constructor. `LoadableHandle` should follow
the same shape.

## Proposed design

### 1. `LoadableHandle<T>` becomes an abstract `Parcelable`
Replace `loader: suspend () -> T` with `protected abstract suspend fun load(): T`.
Concrete subclasses are `@Parcelize` and carry only the load's identity.

```kotlin
@Stable
abstract class LoadableHandle<T> : Parcelable {
    // transient - body properties are NOT parcelled by @Parcelize
    var state: LoadState<T> by mutableStateOf(LoadState.Loading); private set
    private var loadJob: Job? = null
    private var scope: CoroutineScope? = null   // re-supplied on start()

    protected abstract suspend fun load(): T
    fun start(scope: CoroutineScope) { this.scope = scope; if (loadJob == null) launchLoad() }
    fun reload() { /* reuse stored scope */ }
}
```

- An abstract class can't carry `@Parcelize`, but it can **implement** `Parcelable` and let
  each concrete subclass be `@Parcelize`. A `ViewState` property typed as the abstract
  `LoadableHandle<Foo>` parcels via `writeParcelable`/`readParcelable` (concrete CREATOR) -
  the same mechanism states already use for the `ViewStateHolder` interface.

### 2. Scope passed in, not held in the constructor
Pass scope into `start(scope)`; store it in a **transient** field so `reload()` from a button
still works without re-plumbing the scope. Transient is fine - it's re-established on every
`start()`, including post-restore.

### 3. Dependencies via the module `component` accessor
Resolve deps lazily inside `load()` via the impl module's `component` accessor (same one
`ViewStateHolderImpl` uses via `component.inject(this)`). It rebuilds from the DI graph, so
it survives process death.

```kotlin
@Parcelize
internal class SummonerLoadable(val puuid: String, val region: Region) : LoadableHandle<Summoner>() {
    override suspend fun load() = component.summonerRepository.get(puuid, region)
}
```

## Key insight: `LoadState` probably does NOT need to be `Parcelable`
Once `load()` is part of the class, the loaded data is reproducible, so it need not be serialized:
- **Config change** -> ViewModel retains the live handle; `state` + data survive in memory, *no refetch*.
- **Process death** -> handle rebuilt from parcel with `state = Loading`; `start()` re-runs
  `load()` (the `mayakapps.kache` repo cache softens the cost).

Benefits: drops the `T : Parcelable` constraint and avoids `TransactionTooLargeException` from
stuffing payloads into saved-instance-state.

If loaded data *must* survive process death without a refetch, that's a separate opt-in to
layer on later. Default recommendation: do NOT persist loaded data.

## ViewState integration
```kotlin
@Parcelize @State
data class MyState(val loadable: SummonerLoadable) : ViewState {
    @IgnoredOnParcel private var holder: ViewStateHolder? = null
    override fun setParentHolder(p: ViewStateHolder) { holder = p }
    override fun start() { holder?.let { loadable.start(it.viewHolderScope) } }
    // stop(): nothing - holder cancels viewHolderScope children for us
}
```

## `rememberLoadableHandle` (follow-up question)
Drop the lambda-based factory - it doesn't fit an abstract `load()`. Composition-local callers
become `remember { MyLoadable(...) }`. The `Loadable` composable changes to:

```kotlin
val scope = rememberCoroutineScope()
LaunchedEffect(handle) { handle.start(scope) }
```

- Hoisted-in-ViewState handle (already started on the holder's scope): this second `start` is a
  harmless no-op thanks to the `loadJob == null` guard.
- Purely composition-local handle: this `start` kicks off the load.

## Edge case to decide
The `loadJob == null` idempotency guard means a handle whose in-flight job was cancelled by
`stop()` (without process death) would no-op on the next `start()` and stick at `Loading`.
Rare, but if it matters guard on `loadJob?.isActive != true && state !is Loaded` instead.

## Net
- Both original instincts (abstract method instead of lambda; scope passed as argument) are correct.
- Additions: (a) resolve deps via the `component` accessor; (b) `LoadState` can stay non-Parcelable.
- Open decision: refetch on process death (recommended) vs. persist loaded data.
