plugins {
    id("reportmid.android.library")
}

reportMidLib {
    namespace = "com.tsarsprocket.reportmid.matchData.impl"
}

dependencies {
    api(projects.matchData.api)

    implementation(projects.appApi)
    implementation(projects.baseApi)
    implementation(projects.dataDragonApi)
    implementation(projects.lol.api)
    implementation(projects.lolServicesApi)
    implementation(projects.requestManagerApi)
    implementation(projects.utils)

    // KSP
    implementation(projects.kspApi)
    ksp(projects.kspProcessor)

    testImplementation(projects.utilsTest)

    implementation(libs.androidx.core.ktx)

    implementation(libs.mayakapps.kache)

    // Dagger
    kapt(libs.dagger.compiler)
    kapt(libs.dagger.android.processor)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // JUnit 5
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.platform.launcher)
    testRuntimeOnly(libs.junit.jupiter.engine)

    // Mockito
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)

    testImplementation(libs.kotlinx.coroutines.test)
}
