package com.tsarsprocket.reportmid.utils.common

fun Boolean?.orFalse() = this ?: false

fun Boolean?.orTrue() = this ?: true

fun Int?.orZero() = this ?: 0

inline fun <R> Any.tryLogging(action: () -> R): R? = try {
    action()
} catch(exception: Exception) {
    val location = Throwable().stackTrace.firstOrNull()?.let { "[${it.fileName ?: "<unknown file>"}]" }
    logError("Exception in lambda in $location with logging", exception)
    null
}

inline fun <T> T.runIf(condition: Boolean, action: T.() -> T): T = if(condition) action() else this
