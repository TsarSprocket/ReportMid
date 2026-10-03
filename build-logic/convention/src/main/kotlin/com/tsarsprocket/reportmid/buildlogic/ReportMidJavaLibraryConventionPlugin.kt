package com.tsarsprocket.reportmid.buildlogic

import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmExtension

/**
 * Convention plugin for pure-JVM modules (e.g. `utils`, `utilsTest`, `kspProcessor`).
 *
 * Applies the `java-library` and Kotlin JVM plugins and wires up the common ReportMid JVM
 * configuration (Java/Kotlin version, JUnit Platform). Dependencies are declared conventionally
 * in the consuming module's own `dependencies {}` block.
 */
internal class ReportMidJavaLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        val libs = the<LibrariesForLibs>()

        plugins.apply {
            apply("java-library")
            apply(libs.plugins.jetbrains.kotlin.jvm.get().pluginId)
        }

        configure<JavaPluginExtension> {
            sourceCompatibility = JavaVersion.VERSION_18
            targetCompatibility = JavaVersion.VERSION_18
        }

        configure<KotlinJvmExtension> {
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_18)
            }
        }

        tasks.withType<Test> {
            useJUnitPlatform()
        }
    }
}
