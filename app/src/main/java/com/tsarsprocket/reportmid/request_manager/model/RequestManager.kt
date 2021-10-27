package com.tsarsprocket.reportmid.request_manager.model

import io.reactivex.Scheduler
import io.reactivex.Single

interface RequestManager {
    val ioScheduler: Scheduler

    fun<R: RequestResult> addRequest(request: Request<*, R>, scheduler: Scheduler = ioScheduler): Single<R>
    fun cancelRequest(key: RequestKey)
    fun getRequest(key: RequestKey): Request<*, *>?
    fun hasRequest(key: RequestKey): Boolean
}
