plugins {
    id("reportmid.android.library")
}

reportMidLib {
    namespace = "com.tsarsprocket.reportmid.matchData.api"
}

dependencies {
    api(projects.baseApi)
    api(projects.lol.api)
}
