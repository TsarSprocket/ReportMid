plugins {
    id("reportmid.android.library")
}

reportMidLib {
    namespace = "com.tsarsprocket.reportmid.baseImpl"
}

dependencies {
    api(projects.baseApi)
    api(projects.appApi)

    implementation(projects.kspApi)
    ksp(projects.kspProcessor)

    // Dagger
    ksp(libs.dagger.compiler)
    ksp(libs.dagger.android.processor)
}
