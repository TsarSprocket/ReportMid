package com.tsarsprocket.reportmid.gradle

enum class ConfigTypes(val configName: String) {
    API("api"),
    DEBUG("debugImplementation"),
    IMPL("implementation"),
    KAPT("kapt"),
    TEST("testImplementation"),
    ANDROID_TEST("androidTestImplementation")
}