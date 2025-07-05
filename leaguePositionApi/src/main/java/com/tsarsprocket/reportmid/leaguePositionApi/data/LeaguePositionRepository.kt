package com.tsarsprocket.reportmid.leaguePositionApi.data

import com.tsarsprocket.reportmid.leaguePositionApi.model.LeaguePosition
import com.tsarsprocket.reportmid.lol.api.model.Region

interface LeaguePositionRepository {
    suspend fun getAllLeaguePositions(summonerId: String, region: Region): List<LeaguePosition>
    suspend fun getSoloQueueLeaguePosition(summonerId: String, region: Region): LeaguePosition?
}