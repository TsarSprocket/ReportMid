package com.tsarsprocket.reportmid.buildlogic

/**
 * Configuration surface for the `reportmid.android.application` convention plugin.
 *
 * Configure it from a module's `build.gradle.kts`:
 *
 * ```
 * reportMidApp {
 *     appId = "com.tsarsprocket.reportmid.app"
 *     namespace = "com.tsarsprocket.reportmid.appImpl" // optional, defaults to appId
 * }
 * ```
 */
open class ReportMidAppExtension {
    /** Application ID used for `defaultConfig.applicationId`. Required. */
    lateinit var appId: String

    /** Android namespace. Defaults to [appId] when not set. */
    var namespace: String? = null

    /** Extra `BuildConfig` fields to generate, keyed per build type. */
    var buildConfigFields: List<BuildConfigField> = emptyList()

    internal val resolvedNamespace: String
        get() = namespace ?: appId
}
