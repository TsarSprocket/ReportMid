package com.tsarsprocket.reportmid.currentGameData.impl.data

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.currentGameData.api.data.model.CurrentGame
import com.tsarsprocket.reportmid.currentGameData.api.data.model.CurrentGameParticipant
import com.tsarsprocket.reportmid.currentGameData.api.data.model.CurrentTeam
import com.tsarsprocket.reportmid.currentGameData.impl.retrofit.dto.BannedChampionDto
import com.tsarsprocket.reportmid.currentGameData.impl.retrofit.dto.CurrentGameInfoDto
import com.tsarsprocket.reportmid.currentGameData.impl.retrofit.dto.CurrentGameParticipantDto
import com.tsarsprocket.reportmid.currentGameData.impl.retrofit.dto.NoMatchFound
import com.tsarsprocket.reportmid.currentGameData.impl.retrofit.dto.PerksDto
import com.tsarsprocket.reportmid.currentGameData.impl.retrofit.dto.SpectatorResponse
import com.tsarsprocket.reportmid.dataDragonApi.data.DataDragon
import com.tsarsprocket.reportmid.lol.api.domain.GameTypeFactory
import com.tsarsprocket.reportmid.lol.api.domain.model.Rune
import com.tsarsprocket.reportmid.lol.api.domain.model.Runes
import javax.inject.Inject
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@PerApi
internal class CurrentGameMapper @Inject constructor(
    private val gameTypeFactory: GameTypeFactory,
    dataDragon: DataDragon,
) {

    private val tail by lazy { dataDragon.tail }

    @OptIn(ExperimentalTime::class)
    fun map(dto: SpectatorResponse): CurrentGame {
        return when(dto) {
            is CurrentGameInfoDto -> CurrentGame.InProgress(
                gameId = dto.gameId,
                gameType = gameTypeFactory.getGameType(
                    gameMode = dto.gameMode,
                    gameType = dto.gameType,
                    mapId = dto.mapId,
                    queueId = dto.gameQueueConfigId
                ),
                gameStartTime = Instant.fromEpochMilliseconds(dto.gameStartTime),
                teams = mapTeams(dto.participants, dto.bannedChampions)
            )

            is NoMatchFound -> CurrentGame.NotFound
        }
    }

    private fun mapParticipant(participant: CurrentGameParticipantDto): CurrentGameParticipant {
        return CurrentGameParticipant(
            puuid = participant.puuid,
            championId = participant.championId,
            profileIcon = tail.getSummonerImageUrl(participant.profileIconId),
            isBot = participant.bot,
            runes = mapRunes(participant.perks),
            summonerSpell1 = tail.getSummonerSpellById(participant.spell1Id),
            summonerSpell2 = tail.getSummonerSpellById(participant.spell2Id),
        )
    }

    private fun mapRunes(perks: PerksDto): Runes {
        val perkIds = perks.perkIds
        val allRunesCount = PRIMARY_RUNES_COUNT + SECONDARY_RUNES_COUNT
        return Runes(
            primaryRunes = perkIds.take(PRIMARY_RUNES_COUNT).map { tail.getPerkById(it.toInt()) as Rune },
            secondaryRunes = perkIds.subList(PRIMARY_RUNES_COUNT, allRunesCount).map { tail.getPerkById(it.toInt()) as Rune },
            statPerks = perkIds.drop(allRunesCount).map { tail.getPerkById(it.toInt()) }
        )
    }

    private fun mapTeams(participants: List<CurrentGameParticipantDto>, bannedChampions: List<BannedChampionDto>): List<CurrentTeam> {
        val banMap = bannedChampions.fold(emptyMap<Int, List<Int>>()) { acc, ban ->
            val oldBanList = acc.getOrDefault(ban.teamId, emptyList())
            acc + (ban.teamId to oldBanList + ban.championId)
        }

        val participantsByTeamId = participants.fold(emptyMap<Int, List<CurrentGameParticipant>>()) { acc, participant ->
            val mappedParticipant = mapParticipant(participant)
            val teamMembers = acc.getOrDefault(participant.teamId, emptyList())
            acc + (participant.teamId to teamMembers + mappedParticipant)
        }

        return participantsByTeamId.entries.fold(emptyList()) { acc, (teamId, teamMembers) ->
            acc + CurrentTeam(teamId, teamMembers, banMap.getOrDefault(teamId, emptyList()))
        }
    }

    private companion object {
        private const val PRIMARY_RUNES_COUNT = 4
        private const val SECONDARY_RUNES_COUNT = 2
    }
}