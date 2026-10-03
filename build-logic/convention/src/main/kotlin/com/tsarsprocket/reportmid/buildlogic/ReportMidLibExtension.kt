package com.tsarsprocket.reportmid.buildlogic

/**
 * Configuration surface for the `reportmid.android.library` convention plugin.
 *
 * Configure it from a module's `build.gradle.kts`:
 *
 * ```
 * reportMidLib {
 *     namespace = "com.tsarsprocket.reportmid.baseApi"
 * }
 * ```
 *
 * Modules that need Jetpack Compose must additionally apply the
 * `reportmid.android.library.compose` plugin; there is no boolean toggle here
 * because the Compose Kotlin compiler plugin must be applied (or not) at plugin-application
 * time, before this extension is configured.
 */
open class ReportMidLibExtension {
    /** Android namespace. Required. */
    lateinit var namespace: String

    /** Extra `BuildConfig` fields to generate, keyed per build type. */
    var buildConfigFields: List<BuildConfigField> = emptyList()
}
