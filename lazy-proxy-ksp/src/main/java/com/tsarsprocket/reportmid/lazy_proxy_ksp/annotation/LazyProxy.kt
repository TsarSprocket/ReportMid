package com.tsarsprocket.reportmid.lazy_proxy_ksp.annotation

import kotlin.annotation.AnnotationRetention.SOURCE
import kotlin.annotation.AnnotationTarget.CLASS

@Target(CLASS)
@Retention(SOURCE)
annotation class LazyProxy
