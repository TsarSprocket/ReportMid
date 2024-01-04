package com.tsarsprocket.reportmid.utils.common

import android.util.Log

fun Any.logError(msg: String, ex: Throwable? = null) = this::class.simpleName.let { if(ex != null) Log.e(it, msg, ex) else Log.e(it, msg) }

fun Any.logInfo(msg: String, ex: Throwable? = null) = this::class.simpleName.let { if(ex != null) Log.i(it, msg, ex) else Log.i(it, msg) }
