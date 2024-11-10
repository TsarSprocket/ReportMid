// Top-level build file where you can add configuration options common to all sub-projects/modules.

allprojects {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("com.google.devtools.ksp") version "1.9.23-1.0.20" apply false
}

buildscript {
    apply(rootProject.file("versions.gradle.kts"))

    val gradleVersion = "8.5.1"
    val kotlinVersion = "1.9.23"
    val navVersion = "2.7.5"

    repositories {
        google()
        mavenCentral()
//        maven(url = "https://oss.jfrog.org/libs-snapshot")
    }

    dependencies {
        classpath("com.android.tools.build:gradle:$gradleVersion")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$navVersion")
    }
}
