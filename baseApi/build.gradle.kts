plugins {
    id("reportmid.android.library")
}

reportMidLib {
    namespace = "com.tsarsprocket.reportmid.baseApi"
}

dependencies {
    api(projects.utils)

    api(libs.androidx.core.ktx)
    api(libs.androidx.appcompat)
    api(libs.material)
    api(libs.androidx.fragment.ktx)

    // Dagger
    api(libs.dagger.main)
    api(libs.dagger.android)
    api(libs.dagger.android.support)
}
