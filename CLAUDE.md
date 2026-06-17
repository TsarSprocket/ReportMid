# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

ReportMid is a League of Legends personal assistant Android app. It fetches live game data, match history, and summoner info via the Riot API. The project is under active rework — recent commits are
the canonical examples of modern patterns.

## Build Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Run all unit tests
./gradlew test

# Run unit tests for a specific module (e.g. matchData:impl)
./gradlew :matchData:impl:test

# Run a single test class
./gradlew :matchData:impl:test --tests "com.tsarsprocket.reportmid.matchData.impl.data.MatchDataRepositoryImplUnitTest"

# Build & run KSP code generation (happens automatically on build, but forces it)
./gradlew kspDebugKotlin
```

Versions: compileSdk/targetSdk **37**, minSdk **26**, Kotlin **2.2.10**, Gradle **9.2.1**, Java **18**.

## Architecture

### Module Structure

The project uses a strict **api/impl split** for every feature. Modules declare their public contracts in an `*Api` module and hide all implementation details in an `*Impl` module. Other modules may
only depend on `*Api` modules.

**Module types (via `buildSrc`):**

- `library(namespace) { ... }` — Android library module (most modules)
- `application(appId) { ... }` — Android application module (`:appImpl`)
- `javaLibrary { ... }` — Pure JVM module (`:utils`, `:kspProcessor`, `:utilsTest`)

These helpers live in `buildSrc/src/main/java/com/tsarsprocket/reportmid/gradle/ProjectEx.kt` and handle all plugin wiring, SDK versions, and test options automatically.

### Dependency Injection (Dagger 2 + KSP codegen)

Each feature module declares a **Capability** (annotated with `@Capability`), which the custom KSP processor (`kspProcessor`) uses to generate:

- A Dagger `@Component` interface (`<Name>Component`)
- A `@Module` that provisions the component lazily (`<Name>ProvisionModule`)
- A `component.kt` accessor for in-module use

The root DI graph lives in `:appImpl` — `AppApiComponent` aggregates all `*ProvisionModule`s. All feature components are wired as lazy proxies so they're initialized only on first use.

**Key DI annotations:**
| Annotation | Location | Meaning |
|---|---|---|
| `@Capability` | `kspApi` | Declares a feature's DI boundary; triggers codegen |
| `@PerApi` | `baseApi` | Scopes a binding to its capability's component |
| `@AppScope` | `baseApi` | App-singleton scope |
| `@Aggregated` | `baseApi` | Qualifier for aggregated `Map` multibindings |
| `@BindingExport` | `baseApi` | Marks bindings that must be exported up to `AppApiComponent` |
| `@LazyProxy` | `kspApi` | Generates a lazy-delegating wrapper class for a Dagger component |

### View-State MVI (custom framework)

All screens use a custom MVI framework built around `ViewStateHolder` (in `viewStateApi`):

- **`ViewStateHolder`** — owns a `StateFlow<ViewState>`, processes `ViewIntent`s by routing them through registered `ViewStateReducer`s, and renders via `ViewStateVisualizer`s.
- **`ViewStateReducer`** — suspend function `reduce(intent, state, stateHolder): ViewState`; one reducer per `ViewIntent` type.
- **`ViewStateVisualizer`** — `@Composable fun Visualize(modifier, state, stateHolder)` for a set of `ViewState` types.
- **`ViewStateFragment`** — base `Fragment` that hosts a `ViewStateHolder`.

**KSP auto-wiring:** Reducers and visualizers are registered into the DI graph automatically. Annotate with `@Reducer(explicitIntents = [...])` or `@Visualizer(explicitStates = [...])` and the KSP
processor generates the Dagger binding modules. The `@Capability` annotation ties them to the right component.

**Flow for adding a new screen (capability):**

1. Create `<Feature>Api` module — define the public `ViewIntent` (must be `@Parcelize`), and the Dagger `@Component` interface. If the screen needs to navigate out, also declare a `<Feature>Navigation` interface here (see Navigation section).
2. Create `<Feature>Impl` module — define:
    - `InternalViewState` sealed class hierarchy (view states)
    - `Reducer` implementing `ViewStateReducer`, annotated `@Reducer(explicitIntents = [PublicViewIntent::class])`
    - `Visualizer` implementing `ViewStateVisualizer`, annotated `@Visualizer`
    - `<Feature>Capability` interface annotated `@Capability(api = ..., dependencies = [...], modules = [...])`
3. Add `<Feature>CapabilityProvisionModule` to `AppApiComponent` in `:appImpl`.
4. If the screen has a navigation interface, wire it following the steps in the Navigation section.

### Navigation

#### Design principle

Capabilities must not reference each other's types directly. When a capability needs to trigger a transition, it declares a **navigation interface** in its own `*Api` module and calls it. All wiring — which intent actually gets posted to which holder — lives exclusively in `navigationMapImpl`. Any code that directly posts another capability's `ViewIntent` violates this principle and is tech debt to be corrected.

#### Structure

**Navigation interface** (declared in `<Feature>Api`):

```kotlin
interface LandingNavigation {
    fun ViewStateHolder.findSummoner()
    fun ViewStateHolder.proceed(puuid: Puuid, region: Region)

    companion object { const val TAG = "landing_navigation_tag" }
}
```

Rules:
- Methods are extensions on `ViewStateHolder` (the calling holder is implicitly available).
- Methods return `Unit`; suspend is allowed but rare.
- `TAG` in the companion object is the unique Dagger qualifier label.

**`@Navigation` qualifier** (`viewStateApi/.../navigation/Navigation.kt`) — a Dagger `@Qualifier` taking `label: String`. Ties a TAG to an injection site.

**`NavigationMapApi`** (`navigationMapApi/.../NavigationMapApi.kt`) — aggregates every navigation interface, each method annotated `@Navigation(TAG)`:

```kotlin
interface NavigationMapApi {
    @Navigation(LandingNavigation.TAG)
    fun getLandingNavigation(): LandingNavigation
    // one entry per navigation interface
}
```

**`NavigationMapCapability`** (`navigationMapImpl/.../NavigationMapCapability.kt`) — a `@Capability`-annotated interface listing all navigation `@Module`s. KSP generates a Dagger component that implements both `NavigationMapCapability` and `NavigationMapApi`.

**Navigation modules** (`navigationMapImpl/.../di/`) — one `@Module` per navigation interface, each providing a concrete anonymous-object implementation qualified with `@Navigation(TAG)`.

#### Navigation patterns

**1. Direct transition** — post a `ViewIntent` to the calling holder:

```kotlin
override fun ViewStateHolder.proceed(puuid: Puuid, region: Region) =
    postIntent(MainScreenViewIntent(puuid.value, region))
```

**2. Call-and-return** — launch a sub-flow and get a typed result back.

*Forward leg:* pass a `returnIntent` token when calling the sub-flow:
```kotlin
override fun ViewStateHolder.findSummoner() =
    postIntent(FindSummonerViewIntent, returnIntent = LandingIntent.QuitViewIntent)
```
The token is pushed onto the holder's `operationsStack` as a `BackOperation`.

*Return leg:* the sub-capability calls `postReturnIntent`:
```kotlin
// ViewStateHolder.kt extension
fun <M> ViewStateHolder.postReturnIntent(
    processors: Map<Class<out ViewIntent>, Provider<M>>,
    producer: M.() -> ViewIntent,
) {
    postIntent(processors.findProcessor(popTopReturnIntent()).producer())
}
```
`popTopReturnIntent()` removes the token; `findProcessor` does a BFS through the token's class hierarchy to find the right processor; `producer` converts the result into a concrete intent that the *caller's* reducer handles.

*Processor map:* a Dagger multibinding keyed by `@ViewIntentKey` + `@ReturnProcessor(IntentMapper::class)` maps the token type to a caller-specific mapper interface. Register one `@IntoMap` entry per possible caller:

```kotlin
@IntoMap
@ViewIntentKey(LandingIntent::class)       // covers all LandingIntent subtypes via BFS
@ReturnProcessor(FindSummonerResultProcessor::class)
fun provideLandingProcessor() = object : FindSummonerResultProcessor {
    override fun getCancelIntent() = LandingIntent.QuitViewIntent
    override fun getSuccessIntent(puuid: String, region: Region) =
        LandingIntent.SummonerFoundViewIntent(Puuid(puuid), region)
}
```

**3. Hierarchical navigation** — post an intent to a specific *ancestor* holder rather than the current one.

Walk `parentHolder` by state type (using a marker interface such as `SummonerViewStateReturnPoint`) or by tag string (`getTagged(tag)`):

```kotlin
val target = generateSequence(seed = this) { it.parentHolder }
    .find { it.viewStates.value is SummonerViewStateReturnPoint }

target.postIntent(
    intent = MatchDetailsIntent(matchId, region),
    returnIntent = target.viewStates.value.getRestoreStateIntent(),
)
```

The `returnIntent` is produced by the *target's own current state* so returning from the sub-screen restores it exactly.

#### Adding navigation to a new screen

1. Declare the navigation interface in `<Feature>Api` (companion `TAG`, extension methods on `ViewStateHolder`).
2. Add a method annotated `@Navigation(FeatureNavigation.TAG)` to `NavigationMapApi`.
3. Create `<Feature>NavigationModule` in `navigationMapImpl/di/`, implement the interface, add the module to `NavigationMapCapability`.
4. Add `NavigationMapApi::class` to the capability's `dependencies` in `<Feature>Impl`.
5. Inject with `@param:Navigation(FeatureNavigation.TAG)` in the reducer or visualizer that needs it.

### Data Layer

- **Riot API access** via Retrofit services, coordinated by `requestManagerApi`/`requestManagerImpl`.
- **Local persistence** via Room (databases registered in `appImpl`'s `MainDatabase`).
- **DataDragon** (static game assets) has its own `dataDragonApi`/`dataDragonImpl`/`dataDragonRoom`.
- Feature data modules follow a Repository pattern; caching uses `mayakapps.kache`.

### Testing

- Unit tests use **JUnit 5** (`junit.jupiter`). Test configuration (`useJUnitPlatform()`) is applied globally in `buildSrc`.
- `utilsTest` module provides `MainTestDispatcherExtension` — register it with `@RegisterExtension` for coroutine tests.
- Mocking: **Mockito-Kotlin**.
- Tests live in `src/test/java/...` inside each module.

## Key Files

| File                                                | Purpose                                                        |
|-----------------------------------------------------|----------------------------------------------------------------|
| `buildSrc/src/.../ProjectEx.kt`                     | `library {}` / `application {}` / `javaLibrary {}` DSL helpers |
| `buildSrc/src/.../ConfigVersions.kt`                | Centralized SDK versions, minSdk, targetSdk                    |
| `kspProcessor/.../KspProcessor.kt`                  | Custom KSP processor generating Dagger boilerplate             |
| `kspApi/src/.../annotation/`                        | Annotations consumed by the KSP processor                      |
| `viewStateApi/src/.../viewmodel/ViewStateHolder.kt`   | Core MVI runtime interface                                     |
| `viewStateImpl/src/.../ViewStateHolderImpl.kt`        | MVI runtime implementation                                     |
| `appImpl/src/.../di/AppApiComponent.kt`               | Root Dagger component wiring all modules                       |
| `appImpl/src/.../di/AggregatorModule.kt`              | Aggregates all multibinding maps (reducers, visualizers, etc.) |
| `viewStateApi/src/.../navigation/Navigation.kt`       | `@Navigation` Dagger qualifier                                 |
| `viewStateApi/src/.../navigation/ReturnProcessor.kt`  | `@ReturnProcessor` qualifier for call-and-return maps          |
| `navigationMapApi/src/.../NavigationMapApi.kt`        | Aggregated navigation interface (all `@Navigation` bindings)   |
| `navigationMapImpl/src/.../NavigationMapCapability.kt`| `@Capability` wiring all navigation modules                    |
| `navigationMapImpl/src/.../di/`                       | One `@Module` per navigation interface — all inter-cap wiring  |

## Riot API Key

The API key is stored at `lolServicesApi/src/main/res/raw/riot_api_key.txt`. Do not commit a real production key.

## Coding Rules

- **File naming must follow the primary class name.** When the main (or only) class, interface, or object in a file is renamed, the file must be renamed to match at the same time. Create the new
  correctly-named file with the moved content, then delete the old file (use `Remove-Item` via the shell — do not leave an empty stub behind).

- **`Modifier` is the first parameter of a composable when it is the primary modifier.** "Primary" means it controls the layout/appearance of the composable's root element. Default value is `Modifier`. Example:
  ```kotlin
  @Composable
  internal fun ParticipantRow(modifier: Modifier = Modifier, participant: ParticipantInfo) { ... }
  ```

- **Prefer intention-revealing functions over general constructions when they improve clarity without adding unnecessary complexity.** Use named stdlib functions/properties instead of equivalent raw index arithmetic or boolean expressions when the former makes intent clearer. Examples: `first()` over `[0]`, `last()` over `[size-1]`, `lastIndex` over `size-1`, `isEmpty()` over `size == 0`, `getValue(key)` over `map[key]!!`, `indices` over `0 until size`. Apply the same principle to domain code — extract a named property or extension when an expression requires knowledge of a convention to understand.

- **Composable previews live in the same file as the composable, at the very end.** Order within the file: public composable → private helpers → preview. The preview is separated from the preceding
  code by **two** blank lines (not one). The preview function is named `<ComposableName>Preview`. Example:
  ```kotlin
  @Composable
  internal fun Loading(...) { ... }

  private fun helper() { ... }


  @Preview
  @Composable
  private fun LoadingPreview() {
      Loading(...)
  }
  ```

- **Git branch naming.** Prefix bugfix branches with `bugfix/`; prefix all other branches (features, refactors, chores, etc.) with `change/`.

- **Never post another capability's `ViewIntent` directly.** All inter-capability navigation must go through a navigation interface declared in the source capability's `*Api` module and implemented in `navigationMapImpl`. Existing violations of this rule are tech debt — do not add new ones.
