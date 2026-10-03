plugins {
    id("reportmid.android.library")
    id("reportmid.android.library.compose")
}

reportMidLib {
    namespace = "com.tsarsprocket.reportmid.findSummonerImpl"
}

dependencies {
    api(projects.appApi)
    implementation(projects.dataDragonApi)
    api(projects.findSummonerApi)
    implementation(projects.lol.api)
    implementation(projects.summonerApi)
    implementation(projects.theme)
    implementation(projects.utils)
    implementation(projects.navigationMapApi)

    kapt(libs.dagger.compiler)
    kapt(libs.dagger.android.processor)

    implementation(projects.kspApi)
    ksp(projects.kspProcessor)

    implementation(libs.androidx.core.ktx)

    // Compose
    implementation(platform(libs.compose.bom))

    // Compose Material 3
    implementation(libs.compose.material3)

    // Compose preview support
    implementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.tooling.main)

    // Coil
    implementation(libs.coil)
}
