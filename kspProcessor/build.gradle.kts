import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("java-library")
    id(libs.plugins.jetbrains.kotlin.jvm.get().pluginId)
}

repositories {
    gradlePluginPortal()
}

java {
    sourceCompatibility = JavaVersion.VERSION_18
    targetCompatibility = JavaVersion.VERSION_18
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_18)
    }
}

dependencies {
    api(projects.kspApi)
    implementation(libs.symbol.processing.api)
    implementation(libs.kotlin.stdlib)
}
