package com.tsarsprocket.reportmid.league_position_impl.data

import com.tsarsprocket.reportmid.base.common.Mapper
import com.tsarsprocket.reportmid.league_position_api.data.LeaguePositionRepository
import com.tsarsprocket.reportmid.league_position_api.model.LeaguePosition
import com.tsarsprocket.reportmid.league_position_impl.di.LeagueV4ServiceProvider
import com.tsarsprocket.reportmid.league_position_impl.retrofit.LeaguePositionDto
import com.tsarsprocket.reportmid.lol.model.QueueType
import com.tsarsprocket.reportmid.lol.model.Region
import javax.inject.Inject

class LeaguePositionRepositoryImpl @Inject constructor(
    private val leagueV4ServiceProvider: LeagueV4ServiceProvider,
    private val mapper: Mapper<LeaguePositionDto, LeaguePosition>
) : LeaguePositionRepository {

    override suspend fun getAllLeaguePositions(summonerId: String, region: Region): List<LeaguePosition> {
        return leagueV4ServiceProvider(region).getEntriesBySummonerId(summonerId)?.map { mapper(it) }.orEmpty()
    }

    override suspend fun getSoloQueueLeaguePosition(summonerId: String, region: Region): LeaguePosition? {
        return getAllLeaguePositions(summonerId, region).find { it.queueType == QueueType.Solo }
    }
}