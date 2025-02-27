package com.tsarsprocket.reportmid.kspApi.annotation

import com.tsarsprocket.reportmid.kspApi.helper.FindTheOnlyOne
import kotlin.annotation.AnnotationRetention.SOURCE
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.reflect.KClass

@Target(CLASS)
@Retention(SOURCE)
annotation class EffectHandler(
    val effects: Array<KClass<*>> = [],
    val capability: KClass<*> = FindTheOnlyOne::class,
)
