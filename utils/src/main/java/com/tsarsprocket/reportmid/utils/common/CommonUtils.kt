package com.tsarsprocket.reportmid.utils.common

fun Boolean?.orFalse() = this ?: false

fun Boolean?.orTrue() = this ?: true

fun Int?.orZero() = this ?: 0