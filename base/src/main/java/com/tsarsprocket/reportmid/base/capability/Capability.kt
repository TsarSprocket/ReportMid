package com.tsarsprocket.reportmid.base.capability

import javax.inject.Provider

interface Capability<ApiType> {
    val api: ApiType
}