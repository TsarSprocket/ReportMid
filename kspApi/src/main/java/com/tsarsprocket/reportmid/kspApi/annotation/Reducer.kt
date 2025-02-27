package com.tsarsprocket.reportmid.kspApi.annotation

import com.tsarsprocket.reportmid.kspApi.helper.FindTheOnlyOne
import kotlin.annotation.AnnotationRetention.SOURCE
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.reflect.KClass

/**
 * Denotes reducer within module
 * @param intents list here the view intents outside of the module or the ones inside the module that should be handled
 * by the reducer in case there are more than one reducer in the module and the view intents do not refer the reducer from themselves.
 * See also [Intent]
 * @param capability Use this if there is more than one [Capability] in the module (which is not likely though)
 */
@Target(CLASS)
@Retention(SOURCE)
annotation class Reducer(
    val intents: Array<KClass<*>> = [],
    val capability: KClass<*> = FindTheOnlyOne::class,
)
