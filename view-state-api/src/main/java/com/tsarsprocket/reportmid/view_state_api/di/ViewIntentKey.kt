package com.tsarsprocket.reportmid.view_state_api.di

import com.tsarsprocket.reportmid.view_state_api.view_state.ViewIntent
import dagger.MapKey
import kotlin.reflect.KClass

@Target(
    AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewIntentKey(val value: KClass<out ViewIntent>)
