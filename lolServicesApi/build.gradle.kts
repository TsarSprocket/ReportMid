plugins {
    id("reportmid.android.library")
}

reportMidLib {
    namespace = "com.tsarsprocket.reportmid.lolServicesApi"
}

dependencies {
    api(projects.baseApi)
    api(projects.lol.api)
}
