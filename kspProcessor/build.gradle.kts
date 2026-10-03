plugins {
    id("reportmid.jvm.library")
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation(projects.kspApi)
    implementation(libs.symbol.processing.api)
    implementation(libs.kotlin.stdlib)
}
