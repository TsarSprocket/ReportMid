package com.tsarsprocket.reportmid.matchData.api.di

import com.tsarsprocket.reportmid.lol.api.domain.GameTypeFactory
import com.tsarsprocket.reportmid.matchData.api.data.MatchDataRepository

interface MatchDataApi {
    fun getGameTypeFactory(): GameTypeFactory
    fun getMatchDataRepository(): MatchDataRepository
}
