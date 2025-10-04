package com.tsarsprocket.reportmid.matchHistory.impl.domain.interactor

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.dataDragonApi.data.DataDragon
import com.tsarsprocket.reportmid.matchData.api.data.model.HasMoreHint.NO_CHANCE
import com.tsarsprocket.reportmid.matchData.api.data.model.Match
import com.tsarsprocket.reportmid.matchData.api.data.model.Participant
import com.tsarsprocket.reportmid.matchData.api.data.model.Team
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
        val (me, myTeam, teams) = mapTeams(match.teams, playerPuuid)

        return MatchData(
            matchId = match.matchId,
            gameType = match.gameType,
            isRemake = match.isRemake,
            isWin = myTeam.isWinner,
            me = me,
            myTeam = myTeam,
            teams = teams,
            isNotTheLast = match.hasMoreHint != NO_CHANCE,
        )
    }

    private fun mapPlayer(participant: Participant): PlayerData {
        val champion = dragonTail.getChampionById(participant.championId)
        return PlayerData(
            puuid = participant.puuid,
            championIcon = dragonTail.getChampionImageUrl(champion.iconName),
            championName = champion.name,
            kills = participant.kills,
            deaths = participant.deaths,
            assists = participant.assists,
            items = participant.items,
            ward = participant.ward,
        )
    }

    private fun mapTeam(team: Team, myPuuid: String): Pair<PlayerData?, TeamData> {
        var me: PlayerData? = null

        val players = team.participants.map {
            mapPlayer(it)
                .also { playerData ->
                    if(playerData.puuid == myPuuid) me = playerData
                }
        }

        return Pair(me, TeamData(players, team.isWinner))
    }

    private fun mapTeams(teams: List<Team>, myPuuid: String): Triple<PlayerData, TeamData, List<TeamData>> {
        var me: PlayerData? = null
        var myTeam: TeamData? = null

        val allMappedTeams = teams.map { team ->
            val (player, mappedTeam) = mapTeam(team, myPuuid)
            if(player != null) {
                me = player
                myTeam = mappedTeam
            }
            mappedTeam
        }

        return Triple(me!!, myTeam!!, allMappedTeams)
    }
}