package com.tsarsprocket.reportmid.request_manager.model

import com.tsarsprocket.reportmid.di.qualifiers.IoScheduler
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestManagerImpl @Inject constructor(
     @IoScheduler override val ioScheduler: Scheduler,
) : RequestManager {

     private val requestMap: ConcurrentHashMap<RequestKey, RequestInfo> = ConcurrentHashMap()

     override fun<R: RequestResult> addRequest(request: Request<*, R>, scheduler: Scheduler): Single<R> {
          return requestMap[ request.key ]?.let { requestInfo -> requestInfo.single as Single<R> } ?:
               Single.fromCallable { request() }
                    .subscribeOn(scheduler)
                    .doAfterTerminate { requestMap.remove(request.key) }
                    .cache()
                    .also { single -> requestMap[ request.key ] = RequestInfo(request, single) }
     }

     override fun cancelRequest(key: RequestKey) {
          requestMap[ key ]?.let { requestInfo ->
               if( requestInfo.request is CancelableRequest ) requestInfo.request.cancel()
               requestMap.remove( key )
          }
     }

     override fun getRequest(key: RequestKey): Request<*,*>? = requestMap[key]?.request

     override fun hasRequest(key: RequestKey): Boolean = requestMap.contains(key)

     private data class RequestInfo(
          val request: Request<*, *>,
          val single: Single<out RequestResult>,
     )
}