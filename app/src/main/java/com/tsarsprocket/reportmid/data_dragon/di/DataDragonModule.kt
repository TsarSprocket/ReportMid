package com.tsarsprocket.reportmid.data_dragon.di

import com.tsarsprocket.reportmid.data_dragon.model.DataDragon
import com.tsarsprocket.reportmid.data_dragon.model.DataDragonImpl
import com.tsarsprocket.reportmid.di.AppScope
import dagger.Binds
import dagger.Module

@Module
interface DataDragonModule {

    @Binds
    @AppScope
    fun bindDataDragon(dataDragon: DataDragonImpl): DataDragon
}