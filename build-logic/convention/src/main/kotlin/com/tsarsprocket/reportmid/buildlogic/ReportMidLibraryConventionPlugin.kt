package com.tsarsprocket.reportmid.buildlogic

import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.android.build.gradle.LibraryExtension
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
 * Convention plugin for `*Api`/`*Impl`/Room Android library modules.
 *
 * Applies the Android library, Kotlin Android, Kapt, Parcelize and KSP plugins, and wires up the
 * common ReportMid library configuration (SDK versions, compile options, packaging, test
 * options). Module-specific configuration (namespace, BuildConfig fields) is supplied through the
 * [ReportMidLibExtension] registered as `reportMidLib`. Dependencies are declared
 * conventionally in the consuming module's own `dependencies {}` block.
 *
 * Modules that need Jetpack Compose must additionally apply `reportmid.android.library.compose`
 * *after* this plugin in their `plugins {}` block.
 */
internal class ReportMidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        val libs = the<LibrariesForLibs>()

        val extension = extensions.create("reportMidLib", ReportMidLibExtension::class.java)

        plugins.apply {
            apply(libs.plugins.android.library.get().pluginId)
            apply(libs.plugins.jetbrains.kotlin.android.get().pluginId)
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

        extensions.configure<LibraryExtension> {
            compileSdk = ConfigVersions.COMPILE_SDK_VERSION

            defaultConfig {
                minSdk = ConfigVersions.MIN_SDK_VERSION
                lint.targetSdk = ConfigVersions.TARGET_SDK_VERSION

                testInstrumentationRunner = ConfigVersions.TEST_INSTRUMENTATION_RUNNER
                consumerProguardFiles(ConfigVersions.PROGUARD_CONSUMER_RULES)
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

            packaging {
                resources {
                    excludes += ConfigVersions.PACKAGING_EXCLUDES
                }
            }

            buildFeatures {
                buildConfig = true
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

        // `reportMidLib { ... }` is only configured by the consuming build script after
        // this plugin has already applied the base plugins above, so values coming from it
        // (namespace, buildConfigFields) can only be read once the DSL is about to be finalized,
        // via AGP's dedicated extension point for this purpose.
        extensions.configure<LibraryAndroidComponentsExtension> {
            finalizeDsl { androidExtension ->
                androidExtension.namespace = extension.namespace

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
