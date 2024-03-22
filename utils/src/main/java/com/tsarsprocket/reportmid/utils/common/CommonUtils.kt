package com.tsarsprocket.reportmid.utils.common

fun Boolean?.orFalse() = this ?: false

fun Boolean?.orTrue() = this ?: true

fun Int?.orZero() = this ?: 0

fun <R> Any.tryLogging(action: () -> R): R? = try {
    action()
} catch(exception: Exception) {
    logError("Exception in lambda $action", exception); null
}
