plugins {
    id("reportmid.android.library")
}

reportMidLib {
    namespace = "com.tsarsprocket.reportmid.lolRoom"
}

dependencies {
    api(projects.lol.api)

    // Rx
    implementation(libs.rxandroid)

    // Room
    api(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
}
