plugins {
    id("reportmid.android.library")
}

reportMidLib {
    namespace = "com.tsarsprocket.reportmid.stateApi"
}

dependencies {
    api(projects.lol.api)
}
