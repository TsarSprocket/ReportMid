package com.tsarsprocket.reportmid.matchData.impl.data

import com.tsarsprocket.reportmid.dataDragonApi.data.DataDragon
import com.tsarsprocket.reportmid.lol.api.model.GameTypeFactory
import com.tsarsprocket.reportmid.lol.api.model.Perk
import com.tsarsprocket.reportmid.lol.api.model.Rune
import com.tsarsprocket.reportmid.matchData.api.data.model.HasMoreHint
import com.tsarsprocket.reportmid.matchData.api.data.model.Match
import com.tsarsprocket.reportmid.matchData.api.data.model.Participant
import com.tsarsprocket.reportmid.matchData.api.data.model.RuneStyle
import com.tsarsprocket.reportmid.matchData.api.data.model.Team
import com.tsarsprocket.reportmid.matchData.impl.retrofit.MatchDto
import com.tsarsprocket.reportmid.matchData.impl.retrofit.ParticipantDto
import com.tsarsprocket.reportmid.matchData.impl.retrofit.PerkStyleSelectionDto
import com.tsarsprocket.reportmid.matchData.impl.retrofit.PerksDto
import javax.inject.Inject

internal class MatchModelMapper @Inject constructor(
    private val dataDragon: DataDragon,
    private val gameTypeFactory: GameTypeFactory,
) {

    fun map(dto: MatchDto, hasMoreHint: HasMoreHint): Match {
        var isRemake = false
        var winnerTeamId = IMPOSSIBLE_TEAM_ID
        val teamParticipants = dto.info.teams.map { teamDto -> teamDto.teamId }.associateWith { mutableListOf<Participant>() }
        val participants = dto.info.participants.map { participantDto ->
            if(participantDto.gameEndedInEarlySurrender) isRemake = true
            mapParticipant(participantDto).also {
                teamParticipants[participantDto.teamId]?.add(it)
                if(participantDto.win) winnerTeamId = participantDto.teamId
            }
        }
        return Match(
            matchId = dto.metadata.matchId,
            gameType = with(dto.info) { gameTypeFactory.getGameType(gameMode, gameType, mapId, queueId) },
            isRemake = isRemake,
            teams = teamParticipants.entries.map { (teamId, participants) -> Team(participants, teamId == winnerTeamId) },
            participant = participants,
            hasMoreHint = hasMoreHint,
        )
    }

    private fun mapParticipant(dto: ParticipantDto): Participant {

        var primaryStyles: List<Rune>? = null
        var secondaryStyles: List<Rune>? = null

        dto.perks.styles.forEach {
            if(it.selections.size >= PRIMARY_STYLE_SIZE) { // primary style has exactly 3 selections
                primaryStyles = it.selections.map(::mapStyle)
            } else { // secondary presents and has exactly 2 selections
                secondaryStyles = it.selections.map(::mapStyle)
            }
        }

        return with(dto) {
            Participant(
                assists = assists,
                championId = championId,
                deaths = deaths,
                items = listOf(item0, item1, item2, item3, item4, item5),
                ward = item6,
                kills = kills,
                primaryStyle = RuneStyle(primaryStyles!!.first().runePath, primaryStyles), // primary style must present by the game rules
                secondaryStyle = RuneStyle(secondaryStyles!!.first().runePath, secondaryStyles), // secondary style must present by the game rules
                statPerks = perks.map(),
                profileIcon = profileIcon,
                puuid = puuid,
                riotIdGameName = riotIdGameName,
                riotIdTagline = riotIdTagline,
                summoner1Id = summoner1Id,
                summoner2Id = summoner2Id,
                teamPosition = teamPosition,
                visionScore = visionScore,
            )
        }
    }

    private fun PerksDto.map(): List<Perk> = listOf(statPerks.offense, statPerks.defense, statPerks.flex).map(dataDragon.tail::getPerkById)

    private fun mapStyle(dto: PerkStyleSelectionDto) = dataDragon.tail.getPerkById(dto.perk) as Rune

    private companion object {
        const val IMPOSSIBLE_TEAM_ID = -1
        const val PRIMARY_STYLE_SIZE = 3
    }
}
