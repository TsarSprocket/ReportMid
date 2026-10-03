plugins {
    id("reportmid.android.library")
    id("reportmid.android.library.compose")
}

reportMidLib {
    namespace = "com.tsarsprocket.reportmid.summonerViewImpl"
}

dependencies {
    api(projects.summonerViewApi)

    implementation(projects.matchHistory.api)
    implementation(projects.matchUpView.api)
    implementation(projects.navigationMapApi)
    implementation(projects.profileOverviewApi)
    implementation(projects.resLib)
    implementation(projects.theme)

    implementation(projects.kspApi)
    ksp(projects.kspProcessor)

    implementation(libs.androidx.core.ktx)

    kapt(libs.dagger.compiler)
    kapt(libs.dagger.android.processor)

    // Compose
    implementation(platform(libs.compose.bom))

    // Compose Material 3
    implementation(libs.compose.material3)
    implementation(libs.compose.material.icons.core)

    // Compose preview support
    implementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.tooling.main)

    // Optional - Integration with ViewModels
    implementation(libs.androidx.lifecycle.viewmodel.compose)
}
