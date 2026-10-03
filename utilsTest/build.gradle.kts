plugins {
    id("reportmid.jvm.library")
}

dependencies {
    implementation(platform(libs.junit.bom))
    implementation(libs.junit.jupiter.api)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.test)
}
