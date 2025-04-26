package com.tsarsprocket.reportmid.requestManagerImpl.data

import com.tsarsprocket.reportmid.baseApi.di.qualifiers.Io
import com.tsarsprocket.reportmid.requestManagerApi.data.CancelableRequest
import com.tsarsprocket.reportmid.requestManagerApi.data.Request
import com.tsarsprocket.reportmid.requestManagerApi.data.RequestKey
import com.tsarsprocket.reportmid.requestManagerApi.data.RequestResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import kotlin.reflect.KClass
import kotlin.reflect.cast

internal class RequestManagerImpl @Inject constructor(
    @Io val ioDispatcher: CoroutineDispatcher,
) : com.tsarsprocket.reportmid.requestManagerApi.data.RequestManager {

    private val requestMap: ConcurrentHashMap<RequestKey, RequestInfo> = ConcurrentHashMap()

    override suspend fun <R : RequestResult> request(
        request: Request<*, R>,
        clazz: KClass<R>
    ): R = withContext(ioDispatcher) {
        clazz.cast(requestMap[request.key]?.run {
            deferredValue.await()
        } ?: RequestInfo(request, async { request().also { requestMap.remove(request.key) } }).run {
            requestMap[request.key] = this
            deferredValue.await()
        })
    }

    override fun cancelRequest(key: RequestKey) {
        requestMap[key]?.let { requestInfo ->
            if(requestInfo.request is CancelableRequest) requestInfo.request.cancel()
            requestMap.remove(key)
        }
    }

    override fun getRequest(key: RequestKey): Request<*, *>? = requestMap[key]?.request

    override fun hasRequest(key: RequestKey): Boolean = requestMap.contains(key)

    private data class RequestInfo(
        val request: Request<*, *>,
        val deferredValue: Deferred<RequestResult>,
    )
}
