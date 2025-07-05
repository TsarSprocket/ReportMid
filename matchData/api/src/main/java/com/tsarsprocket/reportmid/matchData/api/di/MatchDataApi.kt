package com.tsarsprocket.reportmid.matchData.api.di

import com.tsarsprocket.reportmid.lol.api.model.GameTypeFactory
import com.tsarsprocket.reportmid.matchData.api.data.MatchDataRepository

interface MatchDataApi {
    fun getGameTypeFactory(): GameTypeFactory
    fun getMatchDataRepository(): MatchDataRepository
}
