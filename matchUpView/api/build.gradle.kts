plugins {
    id("reportmid.android.library")
}

reportMidLib {
    namespace = "com.tsarsprocket.reportmid.matchUpView.api"
}

dependencies {
    api(projects.baseApi)
    api(projects.lol.api)
    api(projects.viewStateApi)
}
