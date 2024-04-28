package com.tsarsprocket.reportmid.gradle

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.gradle.LibraryExtension
import com.tsarsprocket.reportmid.gradle.ConfigVersions.COMPILE_SDK_VERSION
import com.tsarsprocket.reportmid.gradle.ConfigVersions.COMPOSE_COMPILER_VERSION
import com.tsarsprocket.reportmid.gradle.ConfigVersions.MIN_SDK_VERSION
import com.tsarsprocket.reportmid.gradle.ConfigVersions.PACKAGING_EXCLUDES
import com.tsarsprocket.reportmid.gradle.ConfigVersions.PROGUARD_CONSUMER_RULES
import com.tsarsprocket.reportmid.gradle.ConfigVersions.PROGUARD_DEFAULT_FILE
import com.tsarsprocket.reportmid.gradle.ConfigVersions.PROGUARD_RULES
import com.tsarsprocket.reportmid.gradle.ConfigVersions.TARGET_SDK_VERSION
import com.tsarsprocket.reportmid.gradle.ConfigVersions.TEST_INSTRUMENTATION_RUNNER
import com.tsarsprocket.reportmid.gradle.ConfigVersions.VERSION_CODE
import com.tsarsprocket.reportmid.gradle.ConfigVersions.VERSION_NAME
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun Project.application(
    appId: String,
    namespace: String = appId,
    dependenciesConfigurator: DependencyHandlerScope.() -> Unit,
) {
    plugins.apply {
        apply("com.android.application")
        apply("org.jetbrains.kotlin.android")
        apply("org.jetbrains.kotlin.kapt")
    }

    configure<ApplicationExtension> {
        this.namespace = namespace
        compileSdk = COMPILE_SDK_VERSION

        defaultConfig {
            applicationId = appId
            minSdk = MIN_SDK_VERSION
            targetSdk = TARGET_SDK_VERSION
            versionCode = VERSION_CODE
            versionName = VERSION_NAME
            testInstrumentationRunner = TEST_INSTRUMENTATION_RUNNER

            vectorDrawables {
                useSupportLibrary = true
            }
        }

        buildTypes {
            release {
                isMinifyEnabled = false
                proguardFiles(getDefaultProguardFile(PROGUARD_DEFAULT_FILE), PROGUARD_RULES)

            }
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_18
            targetCompatibility = JavaVersion.VERSION_18
        }

        buildFeatures {
            compose = true
        }


        composeOptions {
            kotlinCompilerExtensionVersion = COMPOSE_COMPILER_VERSION
        }

        packaging {
            resources {
                excludes += PACKAGING_EXCLUDES
            }
        }
    }

    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_18.toString()
        }
    }

    dependencies {
        dependenciesConfigurator()
    }
}

fun Project.library(namespace: String, dependenciesConfigurator: DependencyHandlerScope.() -> Unit) {

    plugins.apply {
        apply("com.android.library")
        apply("org.jetbrains.kotlin.android")
        apply("org.jetbrains.kotlin.kapt")
    }

    configure<LibraryExtension> {
        this.namespace = namespace
        compileSdk = COMPILE_SDK_VERSION

        defaultConfig {
            minSdk = MIN_SDK_VERSION
            lint.targetSdk = TARGET_SDK_VERSION

            testInstrumentationRunner = TEST_INSTRUMENTATION_RUNNER
            consumerProguardFiles(PROGUARD_CONSUMER_RULES)
        }

        buildTypes {
            release {
                isMinifyEnabled = false
                proguardFiles(getDefaultProguardFile(PROGUARD_DEFAULT_FILE), PROGUARD_RULES)
            }
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_18
            targetCompatibility = JavaVersion.VERSION_18
        }

        buildFeatures {
            compose = true
        }

        composeOptions {
            kotlinCompilerExtensionVersion = COMPOSE_COMPILER_VERSION
        }

        packaging {
            resources {
                excludes += PACKAGING_EXCLUDES
            }
        }
    }

    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_18.toString()
        }
    }

    dependencies {
        dependenciesConfigurator()
    }
}