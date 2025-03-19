package com.tsarsprocket.reportmid.baseApi.di.qualifiers

import androidx.lifecycle.ViewModel
import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.reflect.KClass

@Qualifier
@Retention(RUNTIME)
annotation class DefaultState(val value: KClass<out ViewModel>)
