package com.tsarsprocket.reportmid.requestManagerImpl.data

import com.tsarsprocket.reportmid.baseApi.di.qualifiers.Io
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import kotlin.reflect.KClass
import kotlin.reflect.cast

internal class RequestManagerImpl @Inject constructor(
    @Io val ioDispatcher: CoroutineDispatcher,
) : com.tsarsprocket.reportmid.requestManagerApi.data.RequestManager {

    private val requestMap: ConcurrentHashMap<com.tsarsprocket.reportmid.requestManagerApi.data.RequestKey, RequestInfo> = ConcurrentHashMap()

    override suspend fun <R : com.tsarsprocket.reportmid.requestManagerApi.data.RequestResult> request(
        request: com.tsarsprocket.reportmid.requestManagerApi.data.Request<*, R>,
        clazz: KClass<R>
    ): R {

        suspend fun createNewEntry(request: com.tsarsprocket.reportmid.requestManagerApi.data.Request<*, R>): com.tsarsprocket.reportmid.requestManagerApi.data.RequestResult {
            return RequestInfo(request, coroutineScope { async { request().also { requestMap.remove(request.key) } } }).run {
                requestMap[request.key] = this
                deferredValue.await()
            }
        }

        return withContext(ioDispatcher) {
            clazz.cast(requestMap[request.key]?.run {
                deferredValue.await()
            } ?: createNewEntry(request))
        }
    }

    override fun cancelRequest(key: com.tsarsprocket.reportmid.requestManagerApi.data.RequestKey) {
        requestMap[key]?.let { requestInfo ->
            if(requestInfo.request is com.tsarsprocket.reportmid.requestManagerApi.data.CancelableRequest) requestInfo.request.cancel()
            requestMap.remove(key)
        }
    }

    override fun getRequest(key: com.tsarsprocket.reportmid.requestManagerApi.data.RequestKey): com.tsarsprocket.reportmid.requestManagerApi.data.Request<*, *>? = requestMap[key]?.request

    override fun hasRequest(key: com.tsarsprocket.reportmid.requestManagerApi.data.RequestKey): Boolean = requestMap.contains(key)

    private data class RequestInfo(
        val request: com.tsarsprocket.reportmid.requestManagerApi.data.Request<*, *>,
        val deferredValue: Deferred<com.tsarsprocket.reportmid.requestManagerApi.data.RequestResult>,
    )
}
