plugins {
    id("reportmid.android.library")
}

reportMidLib {
    namespace = "com.tsarsprocket.reportmid.stateImpl"
}

dependencies {
    api(projects.baseApi)
    api(projects.stateApi)
    api(projects.stateRoom)
    api(projects.appApi)

    // Dagger
    ksp(libs.dagger.compiler)
    ksp(libs.dagger.android.processor)
}
