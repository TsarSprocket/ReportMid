plugins {
    id("reportmid.android.library")
}

reportMidLib {
    namespace = "com.tsarsprocket.reportmid.leaguePositionApi"
}

dependencies {
    api(projects.baseApi)
    api(projects.lol.api)
}
