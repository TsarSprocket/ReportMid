package com.tsarsprocket.reportmid.base.di

import dagger.MapKey
import kotlin.reflect.KClass

@Target(
    AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ApiKey(val value: KClass<out Api>)