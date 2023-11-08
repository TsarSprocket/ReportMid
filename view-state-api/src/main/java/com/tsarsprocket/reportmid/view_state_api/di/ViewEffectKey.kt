package com.tsarsprocket.reportmid.view_state_api.di

import com.tsarsprocket.reportmid.view_state_api.view_state.ViewEffect
import dagger.MapKey
import kotlin.reflect.KClass

@Target(
    AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewEffectKey(val value: KClass<out ViewEffect>)
