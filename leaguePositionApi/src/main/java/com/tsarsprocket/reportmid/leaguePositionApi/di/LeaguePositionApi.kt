package com.tsarsprocket.reportmid.leaguePositionApi.di

import com.tsarsprocket.reportmid.leaguePositionApi.data.LeaguePositionRepository

interface LeaguePositionApi {
    fun getLeaguePositionRepository(): LeaguePositionRepository
}