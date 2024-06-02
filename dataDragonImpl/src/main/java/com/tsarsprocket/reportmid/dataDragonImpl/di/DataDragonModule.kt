package com.tsarsprocket.reportmid.dataDragonImpl.di

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.dataDragonApi.data.DataDragon
import com.tsarsprocket.reportmid.dataDragonImpl.data.DataDragonImpl
import dagger.Binds
import dagger.Module

@Module
internal interface DataDragonModule {

    @Binds
    @PerApi
    fun bindDataDragon(dataDragon: DataDragonImpl): DataDragon
}