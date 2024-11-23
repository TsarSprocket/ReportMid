pluginManagement {
    plugins {
        id("com.google.devtools.ksp") version "1.9.23-1.0.20"
        id("com.android.application") version "8.7.2"
        id("com.android.library") version "8.7.2"
        id("org.jetbrains.kotlin.android") version "2.0.0"
        id("org.jetbrains.kotlin.kapt") version "2.0.0"
    }
}

rootProject.name = "ReportMid"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")


include(":app")
include(":appApi")
include(":appImpl")
include(":baseApi")
include(":baseImpl")
include(":dataDragonApi")
include(":dataDragonImpl")
include(":dataDragonRoom")
include(":findSummonerApi")
include(":findSummonerImpl")
include(":kspProcessor")
include(":landingApi")
include(":landingImpl")
include(":leaguePositionApi")
include(":leaguePositionImpl")
include(":lol")
include(":lolResourceLib")
include(":lolRoom")
include(":lolServicesApi")
include(":lolServicesImpl")
include(":navigationMapApi")
include(":navigationMapImpl")
include(":requestManagerApi")
include(":requestManagerImpl")
include(":resLib")
include(":stateApi")
include(":stateImpl")
include(":stateRoom")
include(":summonerApi")
include(":summonerImpl")
include(":summonerRoom")
include(":theme")
include(":utils")
include(":viewStateApi")
include(":viewStateImpl")
