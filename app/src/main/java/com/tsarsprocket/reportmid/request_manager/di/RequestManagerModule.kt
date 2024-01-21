package com.tsarsprocket.reportmid.request_manager.di

import com.tsarsprocket.reportmid.app_api.request_manager.RequestManager
import com.tsarsprocket.reportmid.base.di.AppScope
import com.tsarsprocket.reportmid.request_manager.model.RequestManagerImpl
import dagger.Binds
import dagger.Module

@Module
interface RequestManagerModule {

    @Binds
    @AppScope
    fun bindRequestManager(requestManager: RequestManagerImpl): RequestManager
}