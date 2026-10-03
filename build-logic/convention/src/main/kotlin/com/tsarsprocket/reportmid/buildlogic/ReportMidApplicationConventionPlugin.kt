package com.tsarsprocket.reportmid.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * Convention plugin for the single Android application module.
 *
 * Applies the Android application, Kotlin Android, Compose compiler, Kapt, Parcelize and KSP
 * plugins, and wires up the common ReportMid application configuration (SDK versions, compile
 * options, Compose, packaging, test options). Module-specific configuration (application ID,
 * namespace, BuildConfig fields) is supplied through the [ReportMidAppExtension] registered as
 * `reportMidApp`. Dependencies are declared conventionally in the consuming module's own
 * `dependencies {}` block.
 */
internal class ReportMidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        val libs = the<LibrariesForLibs>()

        val extension = extensions.create("reportMidApp", ReportMidAppExtension::class.java)

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

        extensions.configure<ApplicationExtension> {
            compileSdk = ConfigVersions.COMPILE_SDK_VERSION

            defaultConfig {
                minSdk = ConfigVersions.MIN_SDK_VERSION
                targetSdk = ConfigVersions.TARGET_SDK_VERSION
                versionCode = ConfigVersions.VERSION_CODE
                versionName = ConfigVersions.VERSION_NAME
                testInstrumentationRunner = ConfigVersions.TEST_INSTRUMENTATION_RUNNER

                vectorDrawables {
                    useSupportLibrary = true
                }
            }

            buildTypes {
                release {
                    isMinifyEnabled = false
                    proguardFiles(getDefaultProguardFile(ConfigVersions.PROGUARD_DEFAULT_FILE), ConfigVersions.PROGUARD_RULES)
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
                kotlinCompilerExtensionVersion = ConfigVersions.COMPOSE_COMPILER_VERSION
            }

            packaging {
                resources {
                    excludes += ConfigVersions.PACKAGING_EXCLUDES
                }
            }

            testOptions {
                unitTests {
                    isIncludeAndroidResources = true
                }

                unitTests.all { test ->
                    test.useJUnitPlatform()
                }
            }
        }

        // `reportMidApp { ... }` is only configured by the consuming build script after
        // this plugin has already applied the base plugins above, so values coming from it
        // (namespace, applicationId, buildConfigFields) can only be read once the DSL is about to
        // be finalized, via AGP's dedicated extension point for this purpose.
        extensions.configure<ApplicationAndroidComponentsExtension> {
            finalizeDsl { androidExtension ->
                androidExtension.namespace = extension.resolvedNamespace
                androidExtension.defaultConfig.applicationId = extension.appId

                extension.buildConfigFields.forEach { field ->
                    field.values.entries.forEach { (buildType, value) ->
                        androidExtension.buildTypes.getByName(buildType.buildTypeName).run {
                            buildConfigField(type = field.type, name = field.name, value = value)
                        }
                    }
                }
            }
        }

        tasks.withType<KotlinCompile>().configureEach {
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_18)
            }
        }
    }
}
