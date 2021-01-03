package com.tsarsprocket.reportmid

import android.util.Log

fun Any.logError(msg: String, ex: Throwable? = null) = this::class.simpleName.let { if (ex != null) Log.e(it, msg, ex) else Log.e(it, msg) }