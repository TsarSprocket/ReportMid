plugins {
    id("reportmid.android.library")
    id("reportmid.android.library.compose")
}

reportMidLib {
    namespace = "com.tsarsprocket.reportmid.landingImpl"
}

dependencies {
    api(projects.landingApi)

    implementation(projects.baseApi)
    implementation(projects.appApi)
    implementation(projects.findSummonerApi)
    implementation(projects.lol.api)
    implementation(projects.navigationMapApi)
    implementation(projects.resLib)
    api(projects.theme)
    implementation(projects.stateApi)
    implementation(projects.summonerApi)
    implementation(projects.dataDragonApi)

    implementation(projects.kspApi)
    ksp(projects.kspProcessor)

    implementation(libs.androidx.core.ktx)

    kapt(libs.dagger.compiler)
    kapt(libs.dagger.android.processor)

    // Compose
    implementation(platform(libs.compose.bom))

    // Compose Material 3
    implementation(libs.compose.material3)

    // Compose preview support
    implementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.tooling.main)

    // Optional - Integration with ViewModels
    implementation(libs.androidx.lifecycle.viewmodel.compose)
}
