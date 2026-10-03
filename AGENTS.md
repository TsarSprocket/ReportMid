# AGENTS.md

This file contains repository instructions for OpenAI coding agents working in ReportMid.

## Scope and precedence

- These instructions apply to the entire repository unless a more deeply nested `AGENTS.md` overrides them.
- Follow explicit user instructions first, then the nearest applicable `AGENTS.md`, then existing project style.
- Do not remove or weaken architecture boundaries when making changes.

## Project overview

ReportMid is a League of Legends personal assistant Android app. It fetches live game data, match history, and summoner information via the Riot API.

The project is under active rework. Recent commits are the canonical examples of modern patterns.

## Environment

- Project root: `D:/Develop/ReportMid`
- compileSdk / targetSdk: **37**
- minSdk: **26**
- Kotlin: **2.2.10**
- Gradle: **9.2.1**
- Java: **18**

## Common commands

Run commands from the repository root.

```bash
# Build debug APK
./gradlew assembleDebug

# Run all unit tests
./gradlew test

# Run unit tests for a specific module
./gradlew :matchData:impl:test

# Run a single test class
./gradlew :matchData:impl:test --tests "com.tsarsprocket.reportmid.matchData.impl.data.MatchDataRepositoryImplUnitTest"

# Force KSP code generation; builds normally run this automatically
./gradlew kspDebugKotlin
```

## Architecture

### Module structure

The project uses a strict **api / impl split** for every feature.

- Public contracts live in `*Api` modules.
- Implementations live in `*Impl` modules.
- Other modules may depend only on `*Api` modules, not on another feature's implementation module.
- Implementation details that are not part of an API contract should be `internal`.

Module helpers are Gradle convention plugins defined in the `build-logic` included build (`build-logic/convention/src/main/kotlin/com/tsarsprocket/reportmid/buildlogic/`):

- `id("reportmid.android.library")` + `reportMidLib { namespace = "..." }` — Android library module, used by most modules.
- `id("reportmid.android.library.compose")` — additive plugin enabling Jetpack Compose on a library module; apply it alongside `reportmid.android.library` for modules that need Compose.
- `id("reportmid.android.application")` + `reportMidApp { appId = "..."; namespace = "..." }` — Android application module, used by `:appImpl`.
- `id("reportmid.jvm.library")` — pure JVM module, used by `:utilsTest`. (`:kspProcessor` predates this convention plugin and configures its JVM plugins directly.)

These convention plugins wire the relevant base plugins (Android/Kotlin/Kapt/Parcelize/KSP/Compose), SDK versions, and test options. Dependencies are declared conventionally in each module's own
`dependencies {}` block (`api`, `implementation`, `kapt`, `ksp`, `testImplementation`, etc.) — they are not part of the convention plugins.

### Capability modules

A **capability** is the main feature decomposition unit. Modern capabilities use this nested layout:

```text
<capabilityName>/
├── api/     # public contract; required
├── impl/    # implementation; required, depends on api/
└── room/    # Room schema; optional legacy pattern
```

Older capabilities may use flat module names such as `summonerApi/`, `summonerImpl/`, and `summonerRoom/`. The same rules apply.

## API modules

Purpose: expose only the public contract that other capabilities may consume.

Package root:

```text
com.tsarsprocket.reportmid.<capabilityName>.api
```

Each API module must contain a `di/<CapabilityName>Api.kt` interface. This interface is the single entry point for consuming the capability:

```kotlin
interface MatchDataApi {
    fun getMatchDataRepository(): MatchDataRepository
}
```

Screen-only capabilities that expose no public behavior still need an empty marker interface, for example:

```kotlin
interface MatchUpViewApi
```

Optional public API packages:

- `data/model/` — public domain model classes.
- `navigation/` — navigation interface if the screen needs to navigate out.
- `viewIntent/` — public `ViewIntent` classes. These must be `@Parcelize`.

Typical API module `build.gradle.kts`:

```kotlin
library(namespace = "com.tsarsprocket.reportmid.<capabilityName>.api") {
    with(projects) {
        api(baseApi)       // always
        api(lol.api)       // if public API exposes LoL types
        api(viewStateApi)  // for screen capabilities exposing ViewIntents
    }
}
```

Do not add `impl(...)` dependencies to API modules.

## Impl modules

Purpose: contain all implementation details.

Package root:

```text
com.tsarsprocket.reportmid.<capabilityName>.impl
```

Recommended layout:

```text
<capabilityName>/impl/src/main/java/.../<capabilityName>/impl/
├── di/
│   ├── <CapabilityName>Capability.kt   # @Capability interface; required
│   └── <CapabilityName>Module.kt       # Dagger module; required when bindings are needed
├── data/                               # repositories, mappers, caches
├── retrofit/                           # Retrofit service and DTOs when Riot API is used
│   └── dto/
├── domain/                             # domain logic
├── viewIntent/                         # internal intents for screen capabilities
├── viewState/                          # view-state hierarchy for screen capabilities
├── reducer/                            # @Reducer classes for screen capabilities
└── visualizer/                         # @Visualizer classes for screen capabilities
```

### Capability interface

Each feature implementation declares a `@Capability` interface in `di/`. This triggers the KSP processor to generate:

- `<CapabilityName>Component`, implementing the API interface.
- `<CapabilityName>CapabilityProvisionModule`, consumed by `AppApiComponent`.
- `component.kt`, an in-module accessor.

Example:

```kotlin
@PerApi
@Capability(
    api = MatchDataApi::class,
    dependencies = [
        AppApi::class,        // when @Aggregated multibindings are needed
        DataDragonApi::class, // capability dependencies used here
        LolServicesApi::class,
    ],
    modules = [
        MatchDataModule::class,
    ],
    // exportBindings = [...] // rare; contributes to @BindingExport multibinding
)
internal interface MatchDataCapability
```

### Dagger module

Use a local `@Module` to bind implementations to API interfaces:

```kotlin
@Module
internal interface MatchDataModule {
    @Binds
    @PerApi
    fun bindMatchDataRepository(impl: MatchDataRepositoryImpl): MatchDataRepository
}
```

Typical impl module `build.gradle.kts`:

```kotlin
library(
    namespace = "com.tsarsprocket.reportmid.<capabilityName>.impl",
    enableCompose = true, // only for screen capabilities
) {
    with(projects) {
        api(< capabilityName >.api) // re-export own api module
        impl(appApi)              // when @Aggregated maps are needed
        impl(baseApi)             // always
        impl(kspApi)              // always; @Capability, @Reducer, @Visualizer
        ksp(kspProcessor)         // always; triggers code generation
        // impl(<other>.api) for each capability dependency listed in @Capability
    }
    with(libs) {
        kapt(dagger.compiler)
        kapt(dagger.android.processor)
        // Add Compose, Retrofit, and test dependencies as needed.
    }
}
```

## Room modules

Room modules are **not** capabilities. They are standalone library modules that hold Room entities, DAOs, and a `*StoragePart` interface.

Typical layout:

```text
<capabilityName>Room/src/main/java/.../<capabilityName>Room/
├── <Name>Entity.kt
├── <Name>DAO.kt
└── <Name>StoragePart.kt # interface { fun <name>DAO(): <Name>DAO }
```

Room modules are imported into `appImpl`'s `MainDatabase` to compose the full database schema. The corresponding impl module consumes the room module with `impl(projects.<name>Room)`.

Prefer existing room modules or other persistence strategies before introducing new room modules.

## Wiring capabilities

When adding a new capability:

1. Register modules in `settings.gradle.kts`:
   ```kotlin
   include(":capabilityName:api")
   include(":capabilityName:impl")
   ```
2. Add the generated `<CapabilityName>CapabilityProvisionModule::class` to `AppApiComponent.kt`'s `@Component(modules = [...])` list.
3. If the capability depends on `AppApi::class`, verify that required `@Aggregated` maps are provided by `AggregatorModule`. If a new map type is introduced, add it there and export it through the app
   API.

When consuming another capability's logic:

1. Add the target `*Api` interface to `@Capability(dependencies = [...])`.
2. Add `impl(projects.<targetCapability>.api)` to the consuming impl module's `build.gradle.kts`.
3. Inject the target API interface through Dagger.

When consuming app-wide aggregated functionality:

1. Add `AppApi::class` to `@Capability(dependencies = [...])`.
2. Inject the `@Aggregated`-qualified map. `AggregatorModule` in `appImpl` assembles these maps from all `@BindingExport` contributions.

## Dependency injection

DI uses Dagger 2 plus custom KSP code generation.

The root graph is in `:appImpl`. `AppApiComponent` lists every generated `*CapabilityProvisionModule` in its `@Component(modules = [...])`. Feature components are wired as lazy proxies and initialized
on first use.

Key annotations:

| Annotation       | Module    | Meaning                                                     |
|------------------|-----------|-------------------------------------------------------------|
| `@Capability`    | `kspApi`  | Declares a feature DI boundary and triggers codegen.        |
| `@PerApi`        | `baseApi` | Scopes a binding to a capability component.                 |
| `@AppScope`      | `baseApi` | App-singleton scope.                                        |
| `@Aggregated`    | `baseApi` | Qualifier for aggregated `Map` multibindings.               |
| `@BindingExport` | `baseApi` | Marks bindings exported up to `AppApiComponent`.            |
| `@LazyProxy`     | `kspApi`  | Generates a lazy-delegating wrapper for a Dagger component. |

## View-state MVI

All screens use the custom MVI framework built around `ViewStateHolder` from `viewStateApi`.

Core types:

- `ViewStateHolder` — owns a `StateFlow<ViewState>`, processes `ViewIntent`s through registered reducers, and renders through visualizers.
- `ViewStateReducer` — suspend reducer function: `reduce(intent, state, stateHolder): ViewState`. Use one reducer per `ViewIntent` type.
- `ViewStateVisualizer` — composable visualizer: `@Composable fun Visualize(modifier, state, stateHolder)` for a set of `ViewState` types.
- `ViewStateFragment` — base `Fragment` hosting a `ViewStateHolder`.

Reducers and visualizers are registered automatically by KSP. Annotate classes with:

- `@Reducer(explicitIntents = [...])`
- `@Visualizer(explicitStates = [...])`

The capability annotation ties generated bindings to the correct component.

When adding a new screen capability:

1. Create the API module with:
    - Public `ViewIntent` classes, each `@Parcelize`.
    - Empty marker `<Feature>Api` in `di/` if there is no public behavior.
    - Optional `<Feature>Navigation` interface if the screen navigates out.
2. Create the impl module with:
    - Internal view-state sealed hierarchy in `viewState/`.
    - Reducer in `reducer/`, annotated with `@Reducer(explicitIntents = [PublicViewIntent::class])`.
    - Visualizer in `visualizer/`, annotated with `@Visualizer`.
    - `<Feature>Capability` in `di/`.
    - `<Feature>Module` in `di/` for bindings.
3. Register modules in `settings.gradle.kts` and add the generated provision module to `AppApiComponent`.
4. If the screen has navigation, wire it as described below.

## Navigation

### Rule

Never post another capability's `ViewIntent` directly.

When a capability needs to trigger a transition, it declares a navigation interface in its own `*Api` module and calls that interface. All wiring that decides which intent is posted to which holder
belongs in `navigationMapImpl`.

Existing direct cross-capability intent posts are tech debt. Do not add new ones.

### Navigation interface

Declare navigation interfaces in the source capability API module:

```kotlin
interface LandingNavigation {
    fun ViewStateHolder.findSummoner()
    fun ViewStateHolder.proceed(puuid: Puuid, region: Region)

    companion object {
        const val TAG = "landing_navigation_tag"
    }
}
```

Rules:

- Methods are extensions on `ViewStateHolder`.
- Methods return `Unit`; `suspend` is allowed but rare.
- The companion `TAG` is the unique Dagger qualifier label.

### Navigation map

- `@Navigation(label)` lives in `viewStateApi/.../navigation/Navigation.kt` and qualifies navigation bindings.
- `NavigationMapApi` lives in `navigationMapApi/.../NavigationMapApi.kt` and aggregates every navigation interface:
  ```kotlin
  interface NavigationMapApi {
      @Navigation(LandingNavigation.TAG)
      fun getLandingNavigation(): LandingNavigation
  }
  ```
- `NavigationMapCapability` lives in `navigationMapImpl/.../NavigationMapCapability.kt` and lists all navigation modules.
- Navigation modules live in `navigationMapImpl/.../di/`. Use one `@Module` per navigation interface and provide an anonymous-object implementation qualified with `@Navigation(TAG)`.

### Navigation patterns

Direct transition:

```kotlin
override fun ViewStateHolder.proceed(puuid: Puuid, region: Region) =
    postIntent(MainScreenViewIntent(puuid.value, region))
```

Call-and-return forward leg:

```kotlin
override fun ViewStateHolder.findSummoner() =
    postIntent(FindSummonerViewIntent, returnIntent = LandingIntent.QuitViewIntent)
```

The `returnIntent` token is pushed onto the holder's `operationsStack` as a `BackOperation`.

Call-and-return return leg:

```kotlin
fun <M> ViewStateHolder.postReturnIntent(
    processors: Map<Class<out ViewIntent>, Provider<M>>,
    producer: M.() -> ViewIntent,
) {
    postIntent(processors.findProcessor(popTopReturnIntent()).producer())
}
```

`popTopReturnIntent()` removes the token. `findProcessor` does BFS through the token's class hierarchy to find the right processor. `producer` converts the result into a concrete intent handled by the
caller's reducer.

Processor maps use Dagger multibindings keyed by `@ViewIntentKey` and qualified with `@ReturnProcessor(IntentMapper::class)`:

```kotlin
@IntoMap
@ViewIntentKey(LandingIntent::class) // covers LandingIntent subtypes via BFS
@ReturnProcessor(FindSummonerResultProcessor::class)
fun provideLandingProcessor() = object : FindSummonerResultProcessor {
        override fun getCancelIntent() = LandingIntent.QuitViewIntent
        override fun getSuccessIntent(puuid: String, region: Region) =
            LandingIntent.SummonerFoundViewIntent(Puuid(puuid), region)
    }
```

Hierarchical navigation posts an intent to a specific ancestor holder rather than the current holder. Walk `parentHolder` by state type, marker interface, or tag string:

```kotlin
val target = generateSequence(seed = this) { it.parentHolder }
    .find { it.viewStates.value is SummonerViewStateReturnPoint }

target.postIntent(
    intent = MatchDetailsIntent(matchId, region),
    returnIntent = target.viewStates.value.getRestoreStateIntent(),
)
```

The `returnIntent` should be produced from the target holder's current state so returning restores it exactly.

### Adding navigation to a screen

1. Declare the navigation interface in `<Feature>Api` with a companion `TAG` and extension methods on `ViewStateHolder`.
2. Add a method annotated with `@Navigation(FeatureNavigation.TAG)` to `NavigationMapApi`.
3. Create `<Feature>NavigationModule` in `navigationMapImpl/di/`, implement the interface, and add the module to `NavigationMapCapability`.
4. Add `NavigationMapApi::class` to the source capability's `@Capability(dependencies = [...])`.
5. Inject the interface with `@param:Navigation(FeatureNavigation.TAG)` into the reducer or visualizer that needs it.

## Data layer

- Riot API access uses Retrofit services coordinated by `requestManagerApi` / `requestManagerImpl`.
- Local persistence uses Room. Databases are registered in `appImpl`'s `MainDatabase`.
- DataDragon static game assets live in `dataDragonApi` / `dataDragonImpl` / `dataDragonRoom`.
- Feature data modules follow the repository pattern.
- Caching uses `mayakapps.kache`.

## Testing

- Unit tests use JUnit 5 (`junit.jupiter`). Global test configuration uses `useJUnitPlatform()` in the `build-logic` convention plugins.
- Test source sets live in `src/test/java/...` inside each module.
- Mocking uses Mockito-Kotlin.
- `utilsTest` provides `MainTestDispatcherExtension`; register it with `@RegisterExtension` for coroutine tests.
- Prefer running the smallest relevant Gradle test task before broader test tasks.

## Coding rules

- File names must match the primary class, interface, or object in the file. When renaming a primary declaration, also rename the file. Create the correctly named file with moved content, then delete
  the old file. Do not leave empty stubs.
- For composables, `Modifier` is the first parameter when it is the primary modifier controlling the root layout or appearance. Give it a default value of `Modifier`:
  ```kotlin
  @Composable
  internal fun ParticipantRow(modifier: Modifier = Modifier, participant: ParticipantInfo) { ... }
  ```
- Prefer intention-revealing standard library functions and properties when they improve clarity without unnecessary complexity. Examples: `first()` instead of `[0]`, `last()` instead of `[size - 1]`,
  `lastIndex` instead of `size - 1`, `isEmpty()` instead of `size == 0`, `getValue(key)` instead of `map[key]!!`, and `indices` instead of `0 until size`.
- Apply the same intention-revealing principle to domain code. Extract named properties or extensions when an expression requires convention knowledge to understand.
- Composable previews live in the same file as the composable, at the very end.
    - Order: public composable, private helpers, preview.
    - Separate the preview from preceding code with **two** blank lines.
    - Name the preview `<ComposableName>Preview`.
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
- Branch names: prefix bugfix branches with `bugfix/`; prefix all other branches with `change/`.

## Security

The Riot API key is stored at `lolServicesApi/src/main/res/raw/riot_api_key.txt`. Do not commit a real production key.

## Key files

| File                                                   | Purpose                                                             |
|--------------------------------------------------------|---------------------------------------------------------------------|
| `buildSrc/src/.../ProjectEx.kt`                        | `library {}` / `application {}` / `javaLibrary {}` DSL helpers.     |
| `buildSrc/src/.../ConfigVersions.kt`                   | Centralized SDK versions, minSdk, and targetSdk.                    |
| `kspProcessor/.../KspProcessor.kt`                     | Custom KSP processor generating Dagger boilerplate.                 |
| `kspApi/src/.../annotation/`                           | Annotations consumed by the KSP processor.                          |
| `viewStateApi/src/.../viewmodel/ViewStateHolder.kt`    | Core MVI runtime interface.                                         |
| `viewStateImpl/src/.../ViewStateHolderImpl.kt`         | MVI runtime implementation.                                         |
| `appImpl/src/.../di/AppApiComponent.kt`                | Root Dagger component wiring all modules.                           |
| `appImpl/src/.../di/AggregatorModule.kt`               | Aggregates multibinding maps for reducers, visualizers, etc.        |
| `viewStateApi/src/.../navigation/Navigation.kt`        | `@Navigation` Dagger qualifier.                                     |
| `viewStateApi/src/.../navigation/ReturnProcessor.kt`   | `@ReturnProcessor` qualifier for call-and-return maps.              |
| `navigationMapApi/src/.../NavigationMapApi.kt`         | Aggregated navigation interface for all `@Navigation` bindings.     |
| `navigationMapImpl/src/.../NavigationMapCapability.kt` | `@Capability` wiring all navigation modules.                        |
| `navigationMapImpl/src/.../di/`                        | One `@Module` per navigation interface for inter-capability wiring. |
