package com.tsarsprocket.reportmid.requestManagerApi.data

abstract class CancelableRequest<out K : RequestKey, out R : RequestResult>(key: K) : Request<K, R>(key) {
    abstract fun cancel()
}
