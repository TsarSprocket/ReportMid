package com.tsarsprocket.reportmid.request_manager_api.data

import kotlin.reflect.KClass

interface RequestManager {
    suspend fun <R : RequestResult> request(request: Request<*, R>, clazz: KClass<R>): R
    fun cancelRequest(key: RequestKey)
    fun getRequest(key: RequestKey): Request<*, *>?
    fun hasRequest(key: RequestKey): Boolean
}

suspend inline fun <reified T : RequestResult> RequestManager.request(request: Request<*, T>): T = request(request, T::class)
