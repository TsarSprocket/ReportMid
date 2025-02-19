package com.tsarsprocket.reportmid.viewStateApi.navigation

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.reflect.KClass

@Qualifier
@Retention(RUNTIME)
annotation class ReturnProcessor(val value: KClass<*>)
