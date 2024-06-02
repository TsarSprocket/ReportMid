package com.tsarsprocket.reportmid.kspProcessor.annotation

import kotlin.annotation.AnnotationRetention.SOURCE
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.reflect.KClass

@Target(CLASS)
@Retention(SOURCE)
annotation class Capability(
    val api: KClass<*>,
    val exportBindings: Array<KClass<*>> = [],
    val modules: Array<KClass<*>> = [],
    val dependencies: Array<KClass<*>> = [],
)
