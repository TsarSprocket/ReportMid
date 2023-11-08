package com.tsarsprocket.reportmid.view_state_api.view_state

import kotlin.reflect.KClass

interface Clusterized<T : Any> {
    val clusterClass: KClass<out T>
}