plugins {
    id("reportmid.android.library")
}

reportMidLib {
    namespace = "com.tsarsprocket.reportmid.navigationMapApi"
}

dependencies {
    api(projects.findSummonerApi)
    api(projects.landingApi)
    api(projects.mainScreenApi)
    api(projects.matchDetails.api)
    api(projects.matchHistory.api)
    api(projects.matchUpView.api)
    api(projects.summonerViewApi)
    api(projects.viewStateApi)
}
