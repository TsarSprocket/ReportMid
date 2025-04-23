package com.tsarsprocket.reportmid.matchData.api.di

import com.tsarsprocket.reportmid.matchData.api.data.MatchDataRepository

interface MatchDataApi {

    fun getMatchDataRepository(): MatchDataRepository
}
