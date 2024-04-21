// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    apply(rootProject.file("versions.gradle.kts"))

    val gradleVersion = "8.3.2"
    val kotlinVersion = "1.9.22"
    val navVersion = "2.7.5"

    repositories {
        google()
        mavenCentral()
        maven(url = "https://oss.jfrog.org/libs-snapshot")
    }

    dependencies {
        classpath("com.android.tools.build:gradle:$gradleVersion")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$navVersion")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}