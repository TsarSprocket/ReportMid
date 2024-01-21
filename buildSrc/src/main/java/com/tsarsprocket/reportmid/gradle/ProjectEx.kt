package com.tsarsprocket.reportmid.gradle

import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun Project.library(namespace: String, dependenciesConfigurator: DependencyHandlerScope.() -> Unit) {

    plugins.apply {
        apply("com.android.library")
        apply("org.jetbrains.kotlin.android")
        apply("org.jetbrains.kotlin.kapt")
    }

    configure<LibraryExtension> {
        this.namespace = namespace
        compileSdk = ConfigVersions.COMPILE_SDK_VERSION

        defaultConfig {
            minSdk = ConfigVersions.MIN_SDK_VERSION
            lint.targetSdk = ConfigVersions.TARGET_SDK_VERSION

            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            consumerProguardFiles("consumer-rules.pro")
        }

        buildTypes {
            release {
                isMinifyEnabled = false
                proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            }
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_18
            targetCompatibility = JavaVersion.VERSION_18
        }
    }

    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = "${JavaVersion.VERSION_18}"
        }
    }

    dependencies {
        dependenciesConfigurator()
    }
}