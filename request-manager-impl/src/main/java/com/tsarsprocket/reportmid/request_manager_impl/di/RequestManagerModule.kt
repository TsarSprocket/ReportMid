package com.tsarsprocket.reportmid.request_manager_impl.di

import com.tsarsprocket.reportmid.base.di.PerApi
import com.tsarsprocket.reportmid.request_manager_api.data.RequestManager
import com.tsarsprocket.reportmid.request_manager_impl.data.RequestManagerImpl
import dagger.Binds
import dagger.Module

@Module
internal interface RequestManagerModule {

    @Binds
    @PerApi
    fun bindRequestManager(requestManager: RequestManagerImpl): RequestManager
}