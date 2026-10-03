plugins {
    id("reportmid.android.library")
}

reportMidLib {
    namespace = "com.tsarsprocket.reportmid.matchDetails.api"
}

dependencies {
    implementation(projects.lol.api)
    implementation(projects.viewStateApi)
}
