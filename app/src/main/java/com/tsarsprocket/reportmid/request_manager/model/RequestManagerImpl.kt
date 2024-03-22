package com.tsarsprocket.reportmid.request_manager.model

import com.tsarsprocket.reportmid.app_api.request_manager.CancelableRequest
import com.tsarsprocket.reportmid.app_api.request_manager.Request
import com.tsarsprocket.reportmid.app_api.request_manager.RequestKey
import com.tsarsprocket.reportmid.app_api.request_manager.RequestManager
import com.tsarsprocket.reportmid.app_api.request_manager.RequestResult
import com.tsarsprocket.reportmid.base.di.AppScope
import com.tsarsprocket.reportmid.base.di.qualifiers.Io
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import kotlin.reflect.KClass
import kotlin.reflect.cast

@AppScope
class RequestManagerImpl @Inject constructor(
    @Io val ioDispatcher: CoroutineDispatcher,
) : RequestManager {

    private val requestMap: ConcurrentHashMap<RequestKey, RequestInfo> = ConcurrentHashMap()

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun <R : RequestResult> request(request: Request<*, R>, clazz: KClass<R>): R {

        suspend fun createNewEntry(request: Request<*, R>): RequestResult {
            return RequestInfo(request, coroutineScope { produce { request().also { requestMap.remove(request.key) } } }).run {
                requestMap[request.key] = this
                channel.receive()
            }
        }

        return withContext(ioDispatcher) {
            clazz.cast(requestMap[request.key]?.run {
                try {
                    channel.receive()
                } catch(closedException: ClosedReceiveChannelException) {
                    createNewEntry(request)
                }
            } ?: createNewEntry(request))
        }
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
        val channel: ReceiveChannel<RequestResult>,
    )
}
