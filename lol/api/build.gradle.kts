plugins {
    id("reportmid.android.library")
    id("reportmid.android.library.compose")
}

reportMidLib {
    namespace = "com.tsarsprocket.reportmid.lol.api"
}

dependencies {
    api(projects.utils)

    implementation(projects.kspApi)
    ksp(projects.kspProcessor)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.compose.runtime)
    implementation(libs.material)

    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)

    // JUnit
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.vintage.api)
    testRuntimeOnly(libs.junit.vintage.engine)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
