plugins {
    id("reportmid.android.library")
}

reportMidLib {
    namespace = "com.tsarsprocket.reportmid.requestManagerImpl"
}

dependencies {
    api(projects.baseApi)
    api(projects.requestManagerApi)
    api(projects.appApi)
    api(projects.navigationMapApi)

    implementation(projects.kspApi)
    ksp(projects.kspProcessor)

    // Dagger
    kapt(libs.dagger.compiler)
    kapt(libs.dagger.android.processor)
}
