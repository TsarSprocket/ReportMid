package com.tsarsprocket.reportmid.utils.coroutines

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration

suspend fun <R> doAtLeast(duration: Duration, action: suspend () -> R): R = coroutineScope {
    if(duration.isPositive()) launch { delay(duration) }
    action()
}
