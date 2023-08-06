package com.tsarsprocket.reportmid.base.di

import kotlin.reflect.KClass

interface ApiLocator {
    fun getApi(clazz: Class<out Api>): Api?
}

inline fun <reified T : Api> ApiLocator.getApi(): T? = getApi(T::class.java) as? T