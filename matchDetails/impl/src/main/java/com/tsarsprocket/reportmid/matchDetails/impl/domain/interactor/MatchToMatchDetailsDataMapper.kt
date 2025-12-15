package com.tsarsprocket.reportmid.matchDetails.impl.domain.interactor

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.dataDragonApi.data.DataDragon
import com.tsarsprocket.reportmid.dataDragonApi.data.ItemIsUnknownException
import com.tsarsprocket.reportmid.lol.api.domain.model.Item
import com.tsarsprocket.reportmid.lol.api.domain.model.Region
import com.tsarsprocket.reportmid.lol.api.domain.model.TeamSide
import com.tsarsprocket.reportmid.lol.api.domain.model.UnknownItem
import com.tsarsprocket.reportmid.matchData.api.data.model.Match
import com.tsarsprocket.reportmid.matchData.api.data.model.Participant
import com.tsarsprocket.reportmid.matchData.api.data.model.Team
import com.tsarsprocket.reportmid.matchDetails.impl.domain.model.GameOutcome
import com.tsarsprocket.reportmid.matchDetails.impl.domain.model.MatchDetailsData
import com.tsarsprocket.reportmid.matchDetails.impl.domain.model.PlayerData
import com.tsarsprocket.reportmid.matchDetails.impl.domain.model.TeamData
import javax.inject.Inject

@PerApi
internal class MatchToMatchDetailsDataMapper @Inject constructor(
    private val dataDragon: DataDragon,
) {

    fun map(match: Match, region: Region): MatchDetailsData {
        return MatchDetailsData(
            region = region,
            gameType = match.gameType,
            duration = match.duration,
            teams = match.teams.mapIndexed { index, team -> mapTeam(team, match.isRemake, index) }
        )
    }

    private fun mapTeam(team: Team, isRemake: Boolean, teamPosition: Int): TeamData {
        return TeamData(
            players = team.participants.map { participant -> mapPlayers(participant) },
            gameOutcome = when {
                isRemake -> GameOutcome.REMAKE
                team.isWinner -> GameOutcome.WIN
                else -> GameOutcome.DEFEAT
            },
            teamSide = TeamSide.entries[teamPosition % TeamSide.entries.size]
        )
    }

    private fun mapPlayers(participant: Participant): PlayerData = PlayerData(
        championId = participant.championId,
        championLevel = participant.championLevel,
        gameName = participant.riotIdGameName,
        tagLine = participant.riotIdTagline,
        puuid = participant.puuid,
        summonerLevel = participant.summonerLevel,
        kills = participant.kills,
        deaths = participant.deaths,
        assists = participant.assists,
        primaryRunes = participant.primaryStyle,
        secondaryRunes = participant.secondaryStyle,
        summonerSpells = listOf(participant.summoner1Id, participant.summoner2Id)
            .map { summonerSpellId -> dataDragon.tail.getSummonerSpellById(summonerSpellId.toLong()) },
        items = participant.items.map { itemId -> itemId?.let { mapItem(it) } },
        ward = participant.ward?.let { mapItem(it) },
    )

    private fun mapItem(itemId: Int): Item {
        return try {
            dataDragon.tail.getItemById(itemId)
        } catch(_: ItemIsUnknownException) {
            UnknownItem(itemId)
        }
    }
}
