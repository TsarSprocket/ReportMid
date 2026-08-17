
# ReportMid

> **Status:** ReportMid is under active rework. Some modules are modernized, some older modules still use legacy naming or wiring. Latest commits are the best reference for current patterns.

ReportMid is a League of Legends personal assistant Android app. It is intended to help a player quickly inspect summoner data, current-game context, matchups, match history, and detailed match
information using Riot API data and Data Dragon static assets.

The app is built as a highly modular Android project with a custom capability-based dependency injection layer, a custom MVI framework, and destination-unaware navigation between feature modules.

---

## Main features

ReportMid currently focuses on:

- Finding a League of Legends summoner.
- Loading and storing the selected summoner as the current account.
- Initializing static Data Dragon data such as champions, items, runes, summoner spells, and versions.
- Showing a landing flow while required data is being prepared.
- Opening the main view after the initial account and data setup is complete.
- Displaying summoner-related feature areas:
  - summoner overview,
  - matchup/current-game information,
  - match history,
  - match details,
  - navigation to other summoners from individual views.

The main screen and app bar are still under construction. The app bar has recently been drafted but is not operational yet.

---

## Gradle project modularity

The project uses strict modularity. Most capabilities are split into API and implementation modules, and some also have a Room storage module.

The same architectural rules apply in both cases.

### API modules

API modules expose public contracts only. Other modules should depend on API modules, not implementation modules.

Typical contents:

- Every API module has a single API entry point
- Screen-only APIs may be marker interfaces
- Public view intents belong in API modules and should be `@Parcelize` because the MVI framework passes intents through Parcelable-based holders/backstack mechanics.

### Impl modules

Implementation modules contain internal behavior. Implementation details should usually be `internal`.

Impl modules may depend on:

- their own API module,
- other capability API modules,
- base APIs,
- KSP APIs,
- Dagger/KAPT/KSP infrastructure,
- Retrofit/serialization/Room/etc. as needed.

They should not depend directly on another feature’s implementation module.

### Room modules

Room modules are storage-only modules. They are not capabilities.

Typical contents: Storage parts are composed into the app database in `appImpl`.

Important storage modules include:

- `stateRoom` — current account and global app state.
- `summonerRoom` — summoner/account/friend-related persistence.
- `dataDragonRoom` — champions, items, runes, summoner spells, languages, versions.

---

## Capability and dependency injection architecture

ReportMid uses Dagger 2 plus custom KSP processing.

A **capability** is the runtime DI boundary for a feature. Each modern implementation module declares a capability interface annotated with `@Capability`.

The annotation describes:

- `api` — the public API interface implemented by the generated component.
- `dependencies` — other API components required by this capability.
- `modules` — local Dagger modules used by the capability.
- `exportBindings` — bindings exported upward into app-level aggregated maps.

### Generated Dagger components

KSP processes every `@Capability` and generates Dagger boilerplate, including:

- a Dagger component for the capability,
- a capability provision module,
- lazy accessors/proxies where needed,
- binding modules for reducers, visualizers, effect handlers, and state initializers when annotated classes are present.

That provision module is then registered in the root app component.

### AppApiComponent

The root graph lives in `appImpl`. `AppApiComponent` includes all generated provision modules. The root app component exposes all feature APIs through generated provision modules.

### Aggregated bindings

Some framework features rely on app-level maps assembled from all capabilities.

Examples:

- reducers,
- visualizers,
- effect handlers,
- state initializers,
- return processors.

A capability exports such bindings with `exportBindings`. `AggregatorModule` in `appImpl` combines exported multibindings into app-wide maps consumed by the MVI runtime.

### Lazy proxy mechanism

`@LazyProxy` is used for late binding. It allows components and APIs to be represented by generated lazy-delegating wrappers instead of requiring all capability components to be fully created
immediately.

This helps avoid eager initialization of the whole application graph and makes feature APIs available through stable references while their concrete implementation is initialized on demand.

---

## MVI framework

ReportMid uses a custom MVI framework built around `ViewStateHolder`.

The main concepts are:

- `ViewIntent`
- `ViewState`
- `ViewEffect`
- `ViewStateReducer`
- `ViewStateVisualizer`
- `ViewEffectHandler`
- `ViewStateInitializer`
- `ViewStateHolder`

### ViewIntent

`ViewIntent` represents a user action, navigation action, restore action, or internal command. View intent is always processed on some existing view state. Processing of a view intent results into
some new view state

Examples:

- open screen,
- find summoner,
- restore previous state,
- load more match history,
- show a specific match,
- quit/back intent.

Public intents that cross module boundaries live in API modules.

### ViewState

`ViewState` represents renderable screen state. A view state can be visualized. At the beginning, initial state is always provided

A state may also participate in lifecycle-like behavior through `start()` and `stop()`. When state is changed, the old one is stopped and the new one is started

As result of an intent processing, the same state can be returned by the reducer method. In this case the start/stop logic is not applied

A view state is provided with it's own coroutine context. It can run long-term actions on its context. When view state is stopped, its coroutine context is cleared. To maintain multiple long-term
activities, view state should incorporate MutableState parts and be re-issued by the reducer until all the activities end

`getRestoreStateIntent()` connects state restoration with the navigation/backstack system.

### ViewEffect

`ViewEffect` represents one-off side effects that should not be modeled as persistent state.

Examples:

- show snackbar,
- finish activity,
- go back,
- open external UI.

### ViewStateReducer

Reducers process intents and produce new state. Reducers are usually annotated with `@Reducer`

KSP generates Dagger bindings from reducer annotations.

### ViewStateVisualizer

Visualizers render states using Compose. Visualizers are annotated with `@Visualizer`

KSP generates Dagger bindings from visualizer annotations.

### ViewEffectHandler

Effect handlers process one-off effects.

Handlers can be annotated with `@EffectHandler` and bound automatically by KSP.

### StateInitializer

`ViewStateInitializer` runs setup logic for specific states or holders.

It is useful when opening a state should immediately create child holders, trigger navigation, or start loading nested content.

Example use cases:

- initialize main screen children,
- initialize summoner view sections,
- attach nested holders to a parent holder.

### ViewStateHolder

`ViewStateHolder` owns state, intent dispatch, effects, lifecycle, children, and backstack behavior.

A holder can create child holders. This creates a parent/child hierarchy. It allows complex screens like the main view to be built from nested independently managed sections.

### Backstack and return intents

`ViewStateHolder.postIntent()` can receive an optional `returnIntent`:

The return intent is pushed onto the holder’s internal operations stack. Later, a callee can return data by popping the top return intent and converting it into a caller-specific intent.

---

## Navigation architecture

Navigation is destination-unaware.

A feature does not post another feature’s concrete `ViewIntent` directly. Instead, it depends on a navigation contract exposed by its own or another API module. The actual routing is implemented
centrally in `navigationMapImpl`.

This keeps feature modules decoupled from destination details.

### Declaring a navigation contract

Navigation contracts live in API modules.

Rules:

- methods are extensions on `ViewStateHolder`,
- methods normally return `Unit`,
- each contract has a unique `TAG`,
- the tag is used by the `@Navigation` qualifier.

### Exporting navigation through NavigationMapApi

All navigation contracts are aggregated in `NavigationMapApi`

### Implementing navigation in navigationMapImpl

Navigation implementations live in `navigationMapImpl`

`NavigationMapCapability` includes all navigation modules

### Injecting navigation

A feature that needs navigation declares `NavigationMapApi` as a dependency in its capability. Then it injects the required navigation contract using the tag qualifier and calls it inside
reducer/visualizer/state initializer logic

### Call-and-return navigation

Some flows need the callee to return a result.

Example:

1. Landing opens Find Summoner.
2. Find Summoner lets the user select or cancel.
3. Find Summoner returns a result to Landing.
4. Landing maps the result into its own intent.

Return processors are bound into maps with `@ReturnProcessor` and `@ViewIntentKey`. The processor converts the callee result into the caller’s intent type.

---

## Application architecture

The app broadly follows layered architecture inside each feature

### Repositories

Repositories are public API contracts when other modules need to consume data.

Repository implementations live in impl modules and coordinate:

- remote Riot API calls,
- request deduplication/cancellation through `RequestManager`,
- local persistence through Room,
- mapping DTOs/entities into app domain models,
- caching where useful.

### Business logic layer

Business logic is usually implemented with interactors or use cases.

Interactors keep reducers and visualizers lightweight. They are responsible for app-specific decisions such as:

- which data to load,
- how to combine repository results,
- how to map raw Riot data into UI-ready domain models,
- how to handle missing or partial data.

### Presentation layer

Presentation is MVI-based:

- reducers handle intents,
- states describe the UI,
- visualizers render with Compose,
- effect handlers process one-off effects,
- state initializers create nested screen structures or trigger initial navigation.

### Storage

Persistent storage uses Room. The root database is in `appImpl`

Storage is split into room modules:

- `stateRoom`
  - global state,
  - current account.
- `summonerRoom`
  - summoner cache,
  - my account,
  - friends.
- `dataDragonRoom`
  - champion data,
  - item data,
  - rune data,
  - summoner spell data,
  - languages,
  - versions.

Room modules expose `StoragePart` interfaces that are composed into the main app database.

### Riot API and Data Dragon

The app uses Retrofit and Kotlin serialization/DTO mapping to talk to:

- Riot API services for live/current/player/match data,
- Data Dragon for static game assets.

Riot API access is coordinated by request-related modules

Data Dragon data is loaded and cached locally so screens can render stable names, icons, items, champions, runes, and spells without repeatedly fetching static content

---

## Initial data population flow

The initial user flow is roughly:

The landing feature coordinates this flow. It can navigate to Find Summoner through `LandingNavigation.findSummoner()` and proceed to the main screen through `LandingNavigation.proceed(...)`.

Conceptually:

The purpose is to ensure that the app reaches the main view only after the required summoner and base data are known.

---

## Main view structure

The main view is being redesigned.

The intended structure is:

- Summoner overview
- Matchup/current-game view
- Match history and detail view

### Summoner overview

The summoner overview area shows information about the selected summoner/account.

It is expected to become the default high-level summary area for:

- profile identity
- basic account information
- ranked/profile overview data

### Matchup/current-game view

The matchup area focuses on current-game information and team/enemy comparison.

It uses Riot current-game data and local/static Data Dragon information to show game context in a UI-friendly form.

### Match history and detail view

The match history area lists previous matches for the selected summoner.

The detail view opens deeper data for a single match. It can be reached from match history and can also participate in holder hierarchy/back navigation so the user can return to the previous
summoner/main state.

### Navigating to other summoners

Individual views may allow opening another summoner, for example from:

- current game participants,
- match participants,
- matchup views,
- history/detail views.

---

## Adding a new capability

High-level checklist:

1. Create API module:

- `di/<Feature>Api.kt`,
- public models,
- public view intents,
- navigation contract if needed.

2. Create impl module:

- `di/<Feature>Capability.kt`,
- local Dagger module if bindings are needed,
- reducers,
- visualizers,
- state initializers,
- data/domain/presentation implementation.

3. Annotate the capability

4. Register the generated provision module in `AppApiComponent`

5. If navigation is needed:

- declare navigation interface in the source feature API,
- add it to `NavigationMapApi`,
- implement it in `navigationMapImpl`,
- add the module to `NavigationMapCapability`,
- inject it using `@Navigation(TAG)`.

---

## Technology overview

ReportMid uses:

- Kotlin,
- Android,
- Jetpack Compose,
- Coroutines and Flow,
- Dagger 2,
- custom KSP processor,
- Room,
- Retrofit,
- Kotlin serialization / DTO mapping,
- Riot API,
- Data Dragon.

---

## Notes for contributors

- Keep API/impl boundaries strict.
- Do not depend on another feature’s implementation module.
- Prefer capability APIs and navigation contracts.
- Do not post another feature’s concrete `ViewIntent` directly.
- Put public contracts in API modules.
- Keep implementation details `internal`.
- Prefer the newest modules and latest commits as examples.
