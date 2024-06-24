pluginManagement {
    plugins {
        id("com.google.devtools.ksp") version "1.9.23-1.0.20"
        id("com.android.application") version "8.4.1"
        id("com.android.library") version "8.4.1"
        id("org.jetbrains.kotlin.android") version "2.0.0"
        id("org.jetbrains.kotlin.kapt") version "2.0.0"
    }
}

rootProject.name = "ReportMid"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")


include(":lolResourceLib")
include(":app")
include(":appApi")
include(":appImpl")
include(":landingImpl")
include(":baseApi")
include(":baseImpl")
include(":utils")
include(":lol")
include(":lolServicesImpl")
include(":lolServicesApi")
include(":landingApi")
include(":resLib")
include(":theme")
include(":viewStateApi")
include(":viewStateImpl")
include(":leaguePositionApi")
include(":leaguePositionImpl")
include(":dataDragonApi")
include(":dataDragonImpl")
include(":dataDragonRoom")
include(":summonerApi")
include(":summonerImpl")
include(":summonerRoom")
include(":lolRoom")
include(":stateApi")
include(":stateImpl")
include(":stateRoom")
include(":kspProcessor")
include(":requestManagerApi")
include(":requestManagerImpl")
