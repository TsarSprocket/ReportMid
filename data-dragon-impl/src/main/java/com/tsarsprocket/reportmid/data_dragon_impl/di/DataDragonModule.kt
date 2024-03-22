package com.tsarsprocket.reportmid.data_dragon_impl.di

import com.tsarsprocket.reportmid.base.di.PerApi
import com.tsarsprocket.reportmid.data_dragon_api.data.DataDragon
import com.tsarsprocket.reportmid.data_dragon_impl.data.DataDragonImpl
import dagger.Binds
import dagger.Module

@Module
internal interface DataDragonModule {

    @Binds
    @PerApi
    fun bindDataDragon(dataDragon: DataDragonImpl): DataDragon
}