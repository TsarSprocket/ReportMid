plugins {
    id("reportmid.android.library")
    id("reportmid.android.library.compose")
}

reportMidLib {
    namespace = "com.tsarsprocket.reportmid.matchUpView.impl"
}

dependencies {
    api(projects.matchUpView.api)

    implementation(projects.appApi)
    implementation(projects.baseApi)
    implementation(projects.currentGameData.api)
    implementation(projects.dataDragonApi)
    implementation(projects.navigationMapApi)
    implementation(projects.resLib)
    implementation(projects.summonerApi)
    implementation(projects.theme)
    implementation(projects.utils)
    implementation(projects.viewStateApi)

    // KSP
    implementation(projects.kspApi)
    ksp(projects.kspProcessor)

    // Dagger
    kapt(libs.dagger.compiler)
    kapt(libs.dagger.android.processor)

    // Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.foundation)
    implementation(libs.compose.runtime)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui.tooling.preview)

    // Coil – required to resolve SubcomposeAsyncImageScope / State types in ReloadableImage lambdas
    implementation(libs.coil)
}
