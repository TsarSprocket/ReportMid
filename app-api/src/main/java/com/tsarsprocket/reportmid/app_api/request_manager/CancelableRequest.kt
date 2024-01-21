package com.tsarsprocket.reportmid.app_api.request_manager

abstract class CancelableRequest<out K : RequestKey, out R : RequestResult>(key: K) : Request<K, R>(key) {
    abstract fun cancel()
}
