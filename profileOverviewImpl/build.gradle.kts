plugins {
    id("reportmid.android.library")
    id("reportmid.android.library.compose")
}

reportMidLib {
    namespace = "com.tsarsprocket.reportmid.profileOverviewImpl"
}

dependencies {
    api(projects.profileOverviewApi)
    api(projects.theme)

    implementation(projects.baseApi)
    implementation(projects.dataDragonApi)
    implementation(projects.resLib)
    implementation(projects.summonerApi)
    implementation(projects.utils)

    // KSP
    implementation(projects.kspApi)
    ksp(projects.kspProcessor)

    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.collections.immutable)

    // Dagger
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

    // Coil
    implementation(libs.coil)
}
