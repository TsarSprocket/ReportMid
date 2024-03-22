package com.tsarsprocket.reportmid.utils.data

val <T : Any> T.expiring: ExpiringValue<T>
    get() = ExpiringValue(this)
