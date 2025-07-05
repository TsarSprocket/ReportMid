package com.tsarsprocket.reportmid.lol.impl.di

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.lol.api.model.GameTypeFactory
import com.tsarsprocket.reportmid.lol.api.model.RegionInfo
import com.tsarsprocket.reportmid.lol.impl.model.GameTypeFactoryImpl
import com.tsarsprocket.reportmid.lol.impl.model.RegionInfoFactoryImpl
import dagger.Binds
import dagger.Module

@Module
internal interface LolMainModule {

    @Binds
    @PerApi
    fun bindGameTypeFactory(factoryImpl: GameTypeFactoryImpl): GameTypeFactory

    @Binds
    @PerApi
    fun bindRegionInfoFactory(factoryImpl: RegionInfoFactoryImpl): RegionInfo.Factory
}
