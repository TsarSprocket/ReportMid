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

    ksp(libs.dagger.compiler)
    ksp(libs.dagger.android.processor)
}
