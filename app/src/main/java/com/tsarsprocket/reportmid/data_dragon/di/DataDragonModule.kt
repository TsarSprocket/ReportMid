package com.tsarsprocket.reportmid.data_dragon.di

import com.tsarsprocket.reportmid.data_dragon.model.DataDragon
import com.tsarsprocket.reportmid.data_dragon.model.DataDragonImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface DataDragonModule {

    @Binds
    @Singleton
    fun bindDataDragon(dataDragon: DataDragonImpl): DataDragon
}