plugins {
    id("reportmid.android.library")
}

reportMidLib {
    namespace = "com.tsarsprocket.reportmid.navigationMapImpl"
}

dependencies {
    api(projects.navigationMapApi)

    implementation(projects.baseApi)
    implementation(projects.profileOverviewApi)

    implementation(projects.kspApi)
    ksp(projects.kspProcessor)

    ksp(libs.dagger.compiler)
    ksp(libs.dagger.android.processor)
}
