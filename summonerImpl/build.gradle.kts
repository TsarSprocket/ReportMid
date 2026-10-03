plugins {
    id("reportmid.android.library")
}

reportMidLib {
    namespace = "com.tsarsprocket.reportmid.summonerImpl"
}

dependencies {
    api(projects.summonerApi)
    api(projects.summonerRoom)
    api(projects.dataDragonRoom)
    api(projects.lolServicesApi)
    api(projects.appApi)
    api(projects.requestManagerApi)
    api(projects.utils)

    // Dagger
    kapt(libs.dagger.compiler)
    kapt(libs.dagger.android.processor)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.adapter.rxjava2)
    implementation(libs.converter.gson)

    // Reactive streams
    implementation(libs.reactivestreams)
}
