package com.tsarsprocket.reportmid.matchHistory.impl.domain.interactor

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.dataDragonApi.data.DataDragon
import com.tsarsprocket.reportmid.matchData.api.data.model.HasMoreHint.NO_CHANCE
import com.tsarsprocket.reportmid.matchData.api.data.model.Match
import com.tsarsprocket.reportmid.matchHistory.impl.domain.model.MatchData
import com.tsarsprocket.reportmid.matchHistory.impl.domain.model.PlayerData
import com.tsarsprocket.reportmid.matchHistory.impl.domain.model.TeamData
import javax.inject.Inject

@PerApi
internal class MatchMapper @Inject constructor(
    dataDragon: DataDragon,
) {

    private val dragonTail by lazy { dataDragon.tail }

    fun map(match: Match, playerPuuid: String): MatchData {
        val (allPlayers, me, enemyTeam, myTeam) = mapPlayers(match, playerPuuid)

        return MatchData(
            matchId = match.matchId,
            gameType = match.gameType,
            isRemake = match.isRemake,
            isWin = match.teams.find { team -> team.participants.any { participant -> participant.puuid == playerPuuid } }?.isWinner ?: false,
            allPlayers = allPlayers,
            me = me,
            myTeam = TeamData(myTeam),
            enemyTeam = TeamData(enemyTeam),
            isNotTheLast = match.hasMoreHint != NO_CHANCE,
        )
    }

    private fun mapPlayers(match: Match, playerPuuid: String): SortedPlayers {
        var me: PlayerData? = null
        var myTeam: List<PlayerData>? = null
        var enemyTeam: List<PlayerData>? = null
        val all = mutableListOf<PlayerData>()

        match.teams.onEach { team ->
            var isMine = false
            team.participants.map { participant ->
                val champion = dragonTail.getChampionById(participant.championId)
                PlayerData(
                    puuid = participant.puuid,
                    championIcon = dragonTail.getChampionImageUrl(champion.iconName),
                    championName = champion.name,
                    kills = participant.kills,
                    deaths = participant.deaths,
                    assists = participant.assists,
                    items = participant.items,
                    ward = participant.ward,
                ).also {
                    all += it
                    if(it.puuid == playerPuuid) {
                        me = it
                        isMine = true
                    }
                }
            }.also {
                if(isMine) {
                    myTeam = it
                } else {
                    enemyTeam = it
                }
            }
        }

        return SortedPlayers(
            all = all,
            me = me ?: error("Player $playerPuuid not found in match ${match.matchId}"),
            enemyTeam = enemyTeam ?: error("Enemy team not found in match ${match.matchId}"),
            myTeam = myTeam ?: error("My team not found in match ${match.matchId}"),
        )
    }

    private data class SortedPlayers(
        val all: List<PlayerData>,
        val me: PlayerData,
        val enemyTeam: List<PlayerData>,
        val myTeam: List<PlayerData>,
    )
}