package com.tsarsprocket.reportmid.kspProcessor.annotation

import kotlin.annotation.AnnotationRetention.SOURCE
import kotlin.annotation.AnnotationTarget.CLASS

@Target(CLASS)
@Retention(SOURCE)
annotation class LazyProxy
