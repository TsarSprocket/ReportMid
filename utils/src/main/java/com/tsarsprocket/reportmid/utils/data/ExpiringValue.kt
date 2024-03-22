package com.tsarsprocket.reportmid.utils.data

class ExpiringValue<T>(private val value: T) {
    private val timeStamp = System.currentTimeMillis()

    fun getIfValid(ttl: Long): T? = value.takeIf { (timeStamp + ttl >= System.currentTimeMillis()) }
}
