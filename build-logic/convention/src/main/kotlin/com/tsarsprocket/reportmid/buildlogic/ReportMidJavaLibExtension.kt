package com.tsarsprocket.reportmid.buildlogic

/**
 * Configuration surface for the `reportmid.jvm.library` convention plugin.
 *
 * This is currently a marker with no configurable properties, registered for naming
 * consistency with [ReportMidAppExtension] and [ReportMidLibExtension]. Configure it from a
 * module's `build.gradle.kts` if/when it gains properties:
 *
 * ```
 * reportMidJavaLib { }
 * ```
 */
open class ReportMidJavaLibExtension
