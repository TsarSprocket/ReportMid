plugins {
    id("reportmid.android.library")
}

reportMidLib {
    namespace = "com.tsarsprocket.reportmid.summonerApi"
}

dependencies {
    api(projects.baseApi)
    api(projects.lol.api)

    // Rx
    implementation(libs.rxandroid)
}
