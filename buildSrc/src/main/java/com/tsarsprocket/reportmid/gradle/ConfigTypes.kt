package com.tsarsprocket.reportmid.gradle

enum class ConfigTypes(val configName: String) {
    API("api"),
    DEBUG("debugImplementation"),
    IMPL("implementation"),
    KAPT("kapt"),
    KSP("ksp"),
    TEST("testImplementation"),
    TEST_RUNTIME_ONLY("testRuntimeOnly"),
    ANDROID_TEST("androidTestImplementation"),
}