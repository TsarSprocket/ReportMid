plugins {
    id("reportmid.android.library")
}

reportMidLib {
    namespace = "com.tsarsprocket.reportmid.dataDragonImpl"
}

dependencies {
    api(projects.appApi)
    api(projects.dataDragonApi)
    api(projects.dataDragonRoom)
    api(projects.lolServicesApi)

    // Rx
    implementation(libs.rxandroid)
    implementation(libs.rxkotlin)
    implementation(libs.kotlinx.coroutines.rx2)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.adapter.rxjava2)
    implementation(libs.converter.gson)

    // Dagger
    kapt(libs.dagger.compiler)
    kapt(libs.dagger.android.processor)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
}
