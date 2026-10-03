plugins {
    id("reportmid.android.library")
    id("reportmid.android.library.compose")
}

reportMidLib {
    namespace = "com.tsarsprocket.reportmid.viewStateApi"
}

dependencies {
    api(projects.baseApi)

    implementation(libs.androidx.core.ktx)

    // Compose
    api(platform(libs.compose.bom))
    api(libs.compose.foundation)

    // Compose Material 3
    api(libs.compose.material3)

    // Compose preview support
    implementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.tooling.main)
}
