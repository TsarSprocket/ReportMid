plugins {
    id("reportmid.android.library")
}

reportMidLib {
    namespace = "com.tsarsprocket.reportmid.lol.impl"
}

dependencies {
    api(projects.lol.api)

    implementation(projects.appApi)
    implementation(projects.baseApi)
    implementation(projects.dataDragonApi)
    implementation(projects.utils)

    implementation(projects.kspApi)
    ksp(projects.kspProcessor)

    // Dagger
    ksp(libs.dagger.compiler)
    ksp(libs.dagger.android.processor)
}
