package com.tsarsprocket.reportmid.league_position_api.data

import com.tsarsprocket.reportmid.league_position_api.model.LeaguePosition
import com.tsarsprocket.reportmid.lol.model.Region

interface LeaguePositionRepository {
    suspend fun getAllLeaguePositions(summonerId: String, region: Region): List<LeaguePosition>
    suspend fun getSoloQueueLeaguePosition(summonerId: String, region: Region): LeaguePosition?
}