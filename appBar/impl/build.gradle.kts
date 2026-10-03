plugins {
    id("reportmid.android.library")
    id("reportmid.android.library.compose")
}

reportMidLib {
    namespace = "com.tsarsprocket.reportmid.appBar.impl"
}

dependencies {
    api(projects.appBar.api)

    implementation(projects.appApi)
    implementation(projects.baseApi)
    implementation(projects.theme)
    implementation(projects.viewStateApi)

    implementation(projects.kspApi)
    ksp(projects.kspProcessor)

    implementation(libs.kotlinx.collections.immutable)

    kapt(libs.dagger.compiler)
    kapt(libs.dagger.android.processor)

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.foundation)
    implementation(libs.compose.runtime)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.tooling.main)

    // Coil
    implementation(libs.coil)
}
