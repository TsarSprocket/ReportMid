package com.tsarsprocket.reportmid.kspApi.annotation

import com.tsarsprocket.reportmid.kspApi.helper.FindTheOnlyOne
import kotlin.annotation.AnnotationRetention.SOURCE
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.reflect.KClass

/**
 * Denotes view intent within module
 * @param reducer Put here the reducer class if you want to explicitly bind the view intent to a specific reducer.
 * If there is only one reducer in the module, then it can be bound to the view intent automatically. See also [Reducer]
 */
@Target(CLASS)
@Retention(SOURCE)
annotation class Intent(val reducer: KClass<*> = FindTheOnlyOne::class)
