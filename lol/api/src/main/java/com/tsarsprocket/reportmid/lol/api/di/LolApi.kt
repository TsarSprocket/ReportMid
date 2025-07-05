package com.tsarsprocket.reportmid.lol.api.di

import com.tsarsprocket.reportmid.lol.api.model.GameTypeFactory
import com.tsarsprocket.reportmid.lol.api.model.RegionInfo

interface LolApi {
    fun getGameTypeFactory(): GameTypeFactory
    fun getRegionFactory(): RegionInfo.Factory
}