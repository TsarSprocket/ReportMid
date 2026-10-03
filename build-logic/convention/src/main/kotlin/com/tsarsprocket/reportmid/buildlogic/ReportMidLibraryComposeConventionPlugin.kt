package com.tsarsprocket.reportmid.buildlogic

import com.android.build.gradle.LibraryExtension
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the

/**
 * Add-on convention plugin enabling Jetpack Compose for a `reportmid.android.library` module.
 *
 * Must be applied *after* `reportmid.android.library` in the consuming module's `plugins {}`
 * block, since it configures the [LibraryExtension] registered by that plugin:
 *
 * ```
 * plugins {
 *     id("reportmid.android.library")
 *     id("reportmid.android.library.compose")
 * }
 * ```
 */
internal class ReportMidLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        val libs = the<LibrariesForLibs>()

        plugins.apply(libs.plugins.compose.compiler.get().pluginId)

        extensions.configure<LibraryExtension> {
            buildFeatures {
                compose = true
            }

            composeOptions {
                kotlinCompilerExtensionVersion = ConfigVersions.COMPOSE_COMPILER_VERSION
            }
        }
    }
}
