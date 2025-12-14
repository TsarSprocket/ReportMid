package com.tsarsprocket.reportmid.lol.api.di

import com.tsarsprocket.reportmid.lol.api.domain.GameTypeFactory
import com.tsarsprocket.reportmid.lol.api.domain.model.RegionInfo
import com.tsarsprocket.reportmid.lol.api.presentation.ItemInfoMapper
import com.tsarsprocket.reportmid.lol.api.presentation.SummonerSpellInfoMapper

interface LolApi {
    fun getGameTypeFactory(): GameTypeFactory
    fun getItemInfoMapper(): ItemInfoMapper
    fun getRegionFactory(): RegionInfo.Factory
    fun getSummonerSpellMapper(): SummonerSpellInfoMapper
}