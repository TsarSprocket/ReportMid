package com.tsarsprocket.reportmid.request_manager.model

abstract class CancelableRequest<out K: RequestKey, out R: RequestResult>(key: K) : Request<K,R>(key) {
    abstract fun cancel()
}
