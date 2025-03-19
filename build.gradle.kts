// Top-level build file where you can add configuration options common to all sub-projects/modules.

allprojects {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id(libs.plugins.android.application.get().pluginId) apply false
    id(libs.plugins.android.library.get().pluginId) apply false
    alias(libs.plugins.devtools.ksp) apply false
    id(libs.plugins.jetbrains.kotlin.android.get().pluginId) apply false
    id(libs.plugins.jetbrains.kotlin.kapt.get().pluginId) apply false
    alias(libs.plugins.compose.compiler) apply false
    id(libs.plugins.kotlin.parcelize.get().pluginId) apply false
    id(libs.plugins.jetbrains.kotlin.jvm.get().pluginId) apply false
}

buildscript {
    apply(rootProject.file("versions.gradle.kts"))

    val gradleVersion = "8.9.0"
    val kotlinVersion = "2.1.10"
    val navVersion = "2.7.5"

    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:$gradleVersion")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$navVersion")
    }
}
