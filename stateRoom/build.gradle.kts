plugins {
    id("reportmid.android.library")
}

reportMidLib {
    namespace = "com.tsarsprocket.reportmid.stateRoom"
}

dependencies {
    api(projects.lolRoom)
    implementation(libs.androidx.room.rxjava2)

    // Room
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
}
