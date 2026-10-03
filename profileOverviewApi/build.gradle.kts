plugins {
    id("reportmid.android.library")
}

reportMidLib {
    namespace = "com.tsarsprocket.reportmid.profileOverviewApi"
}

dependencies {
    api(projects.baseApi)
    api(projects.lol.api)
    api(projects.viewStateApi)
}
