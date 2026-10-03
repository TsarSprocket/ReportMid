plugins {
    id("reportmid.android.library")
}

reportMidLib {
    namespace = "com.tsarsprocket.reportmid.currentGameData.api"
}

dependencies {
    api(projects.baseApi)
    api(projects.lol.api)
}
