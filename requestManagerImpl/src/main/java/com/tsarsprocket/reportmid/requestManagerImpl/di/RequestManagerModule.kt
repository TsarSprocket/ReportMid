package com.tsarsprocket.reportmid.requestManagerImpl.di

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.requestManagerApi.data.RequestManager
import com.tsarsprocket.reportmid.requestManagerImpl.data.RequestManagerImpl
import dagger.Binds
import dagger.Module

@Module
internal interface RequestManagerModule {

    @Binds
    @PerApi
    fun bindRequestManager(requestManager: RequestManagerImpl): RequestManager
}