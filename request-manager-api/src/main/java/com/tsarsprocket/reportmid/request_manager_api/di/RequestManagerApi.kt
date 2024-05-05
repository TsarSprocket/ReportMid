package com.tsarsprocket.reportmid.request_manager_api.di

import com.tsarsprocket.reportmid.request_manager_api.data.RequestManager

interface RequestManagerApi {
    fun getRequestManager(): RequestManager
}