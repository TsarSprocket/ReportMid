package com.tsarsprocket.reportmid.viewStateApi.di

import com.tsarsprocket.reportmid.viewStateApi.viewEffect.ViewEffect
import dagger.MapKey
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.FUNCTION
import kotlin.annotation.AnnotationTarget.PROPERTY_GETTER
import kotlin.reflect.KClass


@Target(FUNCTION, PROPERTY_GETTER)
@Retention(RUNTIME)
@MapKey
annotation class ViewEffectKey(val value: KClass<out ViewEffect>)
