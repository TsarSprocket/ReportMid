package com.tsarsprocket.reportmid.league_position_api.di

import com.tsarsprocket.reportmid.league_position_api.data.LeaguePositionRepository

interface LeaguePositionApi {
    fun getLeaguePositionRepository(): LeaguePositionRepository
}