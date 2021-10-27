package com.tsarsprocket.reportmid.request_manager.di

import com.tsarsprocket.reportmid.request_manager.model.RequestManager
import com.tsarsprocket.reportmid.request_manager.model.RequestManagerImpl
import dagger.Binds
import dagger.Module

@Module
interface RequestManagerModule {

    @Binds
    fun bindRequestManager(requestManager: RequestManagerImpl): RequestManager
}