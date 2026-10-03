plugins {
    id("reportmid.android.library")
}

reportMidLib {
    namespace = "com.tsarsprocket.reportmid.appBar.api"
}

dependencies {
    api(projects.baseApi)
    api(projects.viewStateApi)
}
