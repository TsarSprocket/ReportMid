plugins {
    id("reportmid.android.library")
}

reportMidLib {
    namespace = "com.tsarsprocket.reportmid.leaguePositionImpl"
}

dependencies {
    api(projects.leaguePositionApi)
    api(projects.lolServicesApi)
    implementation(projects.utils)

    implementation(projects.kspApi)
    ksp(projects.kspProcessor)

    // Retrofit
    implementation(libs.retrofit)

    // Dagger
    kapt(libs.dagger.compiler)
    kapt(libs.dagger.android.processor)
}
