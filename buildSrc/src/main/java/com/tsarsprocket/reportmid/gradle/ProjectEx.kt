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
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.the
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun Project.application(
    appId: String,
    namespace: String = appId,
    buildConfigFields: List<BuildConfigField> = emptyList(),
    dependenciesConfigurator: DependencyHandlerScope.() -> Unit,
) {
    val libs = the<LibrariesForLibs>()

    plugins.apply {
        apply(libs.plugins.android.application.get().pluginId)
        apply(libs.plugins.jetbrains.kotlin.android.get().pluginId)
        apply(libs.plugins.compose.compiler.get().pluginId)
        apply(libs.plugins.jetbrains.kotlin.kapt.get().pluginId)
        apply(libs.plugins.kotlin.parcelize.get().pluginId)
        apply(libs.plugins.devtools.ksp.get().pluginId)
    }

    configure<KotlinProjectExtension> {
        sourceSets.getByName("main").kotlin {
            srcDir("build/generated/ksp/debug/kotlin")
            srcDir("build/generated/ksp/release/kotlin")
        }
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

            buildConfigFields.forEach { buildConfigField ->
                buildConfigField.values.entries.forEach { (buildType, value) ->
                    getByName(buildType.buildTypeName).run {
                        buildConfigField(type = buildConfigField.type, name = buildConfigField.name, value = value)
                    }
                }
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

fun Project.library(
    namespace: String,
    enableCompose: Boolean = false,
    buildConfigFields: List<BuildConfigField> = emptyList(),
    dependenciesConfigurator: DependencyHandlerScope.() -> Unit = {},
) {
    val libs = the<LibrariesForLibs>()

    plugins.apply {
        apply(libs.plugins.android.library.get().pluginId)
        apply(libs.plugins.jetbrains.kotlin.android.get().pluginId)
        if(enableCompose) apply(libs.plugins.compose.compiler.get().pluginId)
        apply(libs.plugins.jetbrains.kotlin.kapt.get().pluginId)
        apply(libs.plugins.kotlin.parcelize.get().pluginId)
        apply(libs.plugins.devtools.ksp.get().pluginId)
    }

    configure<KotlinProjectExtension> {
        sourceSets.getByName("main").kotlin {
            srcDir("build/generated/ksp/debug/kotlin")
            srcDir("build/generated/ksp/release/kotlin")
        }
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

            buildConfigFields.forEach { buildConfigField ->
                buildConfigField.values.entries.forEach { (buildType, value) ->
                    getByName(buildType.buildTypeName).run {
                        buildConfigField(type = buildConfigField.type, name = buildConfigField.name, value = value)
                    }
                }
            }
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_18
            targetCompatibility = JavaVersion.VERSION_18
        }

        if(enableCompose) {
            buildFeatures {
                compose = true
            }

            composeOptions {
                kotlinCompilerExtensionVersion = COMPOSE_COMPILER_VERSION
            }
        }

        packaging {
            resources {
                excludes += PACKAGING_EXCLUDES
            }
        }

        buildFeatures {
            buildConfig = true
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