package com.tsarsprocket.reportmid.utils.lazy

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ClearableLazy<T>(private val initializer: () -> T) : ReadOnlyProperty<Any?, T> {

    private var value: Any? = UNINITIALIZED

    @Synchronized
    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        if (value === UNINITIALIZED) value = initializer()
        return value as T
    }

    @Synchronized
    fun clear() {
        value = UNINITIALIZED
    }

    companion object {
        private val UNINITIALIZED = Any()
    }
}

fun <T> clearableLazy(initializer: () -> T): ClearableLazy<T> = ClearableLazy(initializer)
