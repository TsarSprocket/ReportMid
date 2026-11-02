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
    ksp(libs.dagger.compiler)
    ksp(libs.dagger.android.processor)
}
