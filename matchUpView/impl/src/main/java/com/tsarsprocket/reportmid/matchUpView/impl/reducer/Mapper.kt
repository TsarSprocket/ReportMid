package com.tsarsprocket.reportmid.matchUpView.impl.reducer

import com.tsarsprocket.reportmid.dataDragonApi.data.DataDragon
import com.tsarsprocket.reportmid.lol.api.domain.model.Region
import com.tsarsprocket.reportmid.matchUpView.impl.domain.model.CurrentMatchUp
import com.tsarsprocket.reportmid.matchUpView.impl.domain.model.Participant
import com.tsarsprocket.reportmid.matchUpView.impl.domain.model.Team
import com.tsarsprocket.reportmid.matchUpView.impl.viewState.MatchUpState
import com.tsarsprocket.reportmid.matchUpView.impl.viewState.ParticipantInfo
import com.tsarsprocket.reportmid.matchUpView.impl.viewState.TeamInfo
import javax.inject.Inject

internal class Mapper @Inject constructor(
    dataDragon: DataDragon,
) {

    private val tail by lazy { dataDragon.tail }

    fun map(from: CurrentMatchUp, puuid: String, region: Region): MatchUpState {
        return MatchUpState(
            puuid = puuid,
            region = region,
            teams = from.teams.map { mapTeam(it) },
        )
    }

    private fun mapTeam(from: Team): TeamInfo {
        return TeamInfo(
            isBlueSide = from.id == BLUE_TEAM_ID,
            participants = from.participants.map { mapParticipant(it) },
        )
    }

    private fun mapParticipant(from: Participant): ParticipantInfo {
        return ParticipantInfo(
            championImageUrl = tail.getChampionImageUrl(from.champion.iconName),
            summonerDisplayName = formatDisplayName(from),
            primaryRuneImageUrls = from.runes.primaryRunes.map { tail.getRuneImageUrl(it) },
            secondaryRuneImageUrls = from.runes.secondaryRunes.map { tail.getRuneImageUrl(it) },
            summonerSpell1ImageUrl = tail.getSummonerSpellImageUrl(from.summonerSpell1.iconName),
            summonerSpell2ImageUrl = tail.getSummonerSpellImageUrl(from.summonerSpell2.iconName),
        )
    }

    private fun formatDisplayName(participant: Participant): String = when {
        participant.isBot -> "Bot"
        participant.gameName != null && participant.tagLine != null -> "${participant.gameName}#${participant.tagLine}"
        participant.gameName != null -> participant.gameName
        else -> participant.puuid.take(8)
    }

    private companion object {
        const val BLUE_TEAM_ID = 100
    }
}
