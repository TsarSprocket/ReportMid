package com.tsarsprocket.reportmid.lol.impl.di

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.lol.api.domain.GameTypeFactory
import com.tsarsprocket.reportmid.lol.api.domain.model.RegionInfo
import com.tsarsprocket.reportmid.lol.api.presentation.ItemInfoMapper
import com.tsarsprocket.reportmid.lol.api.presentation.SummonerSpellInfoMapper
import com.tsarsprocket.reportmid.lol.impl.domain.GameTypeFactoryImpl
import com.tsarsprocket.reportmid.lol.impl.domain.RegionInfoFactoryImpl
import com.tsarsprocket.reportmid.lol.impl.presentation.ItemInfoMapperImpl
import com.tsarsprocket.reportmid.lol.impl.presentation.SummonerSpellInfoMapperImpl
import dagger.Binds
import dagger.Module

@Module
internal interface LolMainModule {

    @Binds
    @PerApi
    fun bindGameTypeFactory(factoryImpl: GameTypeFactoryImpl): GameTypeFactory

    @Binds
    @PerApi
    fun bindItemInfoMapper(mapper: ItemInfoMapperImpl): ItemInfoMapper

    @Binds
    @PerApi
    fun bindRegionInfoFactory(factoryImpl: RegionInfoFactoryImpl): RegionInfo.Factory

    @Binds
    @PerApi
    fun bindSummonerSpellMapper(mapper: SummonerSpellInfoMapperImpl): SummonerSpellInfoMapper
}
