plugins {
    id("reportmid.android.library")
}

reportMidLib {
    namespace = "com.tsarsprocket.reportmid.currentGameData.impl"
}

dependencies {
    api(projects.currentGameData.api)

    implementation(projects.dataDragonApi)
    implementation(projects.lol.api)
    implementation(projects.lolServicesApi)
    implementation(projects.requestManagerApi)

    // KSP
    implementation(projects.kspApi)
    ksp(projects.kspProcessor)

    // Dagger
    kapt(libs.dagger.compiler)
    kapt(libs.dagger.android.processor)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
}
