plugins {
    id("reportmid.android.library")
}

reportMidLib {
    namespace = "com.tsarsprocket.reportmid.dataDragonApi"
}

dependencies {
    api(projects.baseApi)
    api(projects.lol.api)

    // Rx
    implementation(libs.rxandroid)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
}
