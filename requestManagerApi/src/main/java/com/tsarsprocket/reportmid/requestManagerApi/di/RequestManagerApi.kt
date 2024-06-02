package com.tsarsprocket.reportmid.requestManagerApi.di

import com.tsarsprocket.reportmid.requestManagerApi.data.RequestManager

interface RequestManagerApi {
    fun getRequestManager(): RequestManager
}