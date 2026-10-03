plugins {
    id("reportmid.android.library")
}

reportMidLib {
    namespace = "com.tsarsprocket.reportmid.findSummonerApi"
}

dependencies {
    api(projects.baseApi)
    api(projects.viewStateApi)
    api(projects.lol.api)

    kapt(libs.dagger.compiler)
    kapt(libs.dagger.android.processor)
}
