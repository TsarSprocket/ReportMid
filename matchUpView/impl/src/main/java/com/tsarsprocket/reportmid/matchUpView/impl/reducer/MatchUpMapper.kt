package com.tsarsprocket.reportmid.matchUpView.impl.reducer

import com.tsarsprocket.reportmid.dataDragonApi.data.DataDragon
import com.tsarsprocket.reportmid.lol.api.domain.model.Region
import com.tsarsprocket.reportmid.matchUpView.impl.domain.model.CurrentMatchUp
import com.tsarsprocket.reportmid.matchUpView.impl.domain.model.Participant
import com.tsarsprocket.reportmid.matchUpView.impl.domain.model.Team
import com.tsarsprocket.reportmid.matchUpView.impl.viewState.BotPlayerInfo
import com.tsarsprocket.reportmid.matchUpView.impl.viewState.KnownPlayerInfo
import com.tsarsprocket.reportmid.matchUpView.impl.viewState.MatchUpState
import com.tsarsprocket.reportmid.matchUpView.impl.viewState.ParticipantInfo
import com.tsarsprocket.reportmid.matchUpView.impl.viewState.PlayerInfo
import com.tsarsprocket.reportmid.matchUpView.impl.viewState.RuneIconInfo
import com.tsarsprocket.reportmid.matchUpView.impl.viewState.TeamInfo
import com.tsarsprocket.reportmid.matchUpView.impl.viewState.UnknownPlayerInfo
import com.tsarsprocket.reportmid.viewStateApi.viewState.LoadablePart
import javax.inject.Inject

internal class MatchUpMapper @Inject constructor(
    dataDragon: DataDragon,
) {

    private val tail by lazy { dataDragon.tail }

    fun map(
        from: CurrentMatchUp,
        puuid: String,
        region: Region,
        accountLoadTrigger: AccountLoadTrigger,
        summonerLoadTrigger: SummonerLoadTrigger,
    ): MatchUpState {
        val teams = from.teams.associate { it.id to mapTeam(it, accountLoadTrigger, summonerLoadTrigger) }
        val teamsList = teams.values.toList()
        val puuidTeamIndex = teamsList.indexOfFirst { it.participants.containsKey(puuid) }
        val initialSelectedTeamIndex = if (puuidTeamIndex == 0) 1 else 0
        return MatchUpState(
            puuid = puuid,
            region = region,
            teams = teams,
            initialSelectedTeamIndex = initialSelectedTeamIndex,
        )
    }

    private fun mapTeam(from: Team, accountLoadTrigger: AccountLoadTrigger, summonerLoadTrigger: SummonerLoadTrigger): TeamInfo {
        return TeamInfo(
            id = from.id,
            isBlueSide = from.id == BLUE_TEAM_ID,
            participants = from.participants.mapIndexed { index, participant ->
                val info = mapParticipant(participant,
                    { puuid -> accountLoadTrigger(from.id, puuid) },
                    { puuid -> summonerLoadTrigger(from.id, puuid) },
                )
                (participant.puuid ?: "synthetic_$index") to info
            }.toMap(),
        )
    }

    private fun mapParticipant(
        from: Participant,
        accountLoadTrigger: (String) -> Unit,
        summonerLoadTrigger: (String) -> Unit,
    ): ParticipantInfo = ParticipantInfo(
        player = mapPlayer(from, accountLoadTrigger, summonerLoadTrigger),
        championImageUrl = tail.getChampionImageUrl(from.champion.iconName),
        primaryRune = RuneIconInfo(
            imageUrl = tail.getRuneImageUrl(from.runes.rune),
            title = from.runes.rune.name,
        ),
        secondaryRuneStyle = RuneIconInfo(
            imageUrl = tail.getRunePathImageUrl(from.runes.secondaryStyle),
            title = from.runes.secondaryStyle.name,
        ),
        summonerSpell1ImageUrl = tail.getSummonerSpellImageUrl(from.summonerSpell1.iconName),
        summonerSpell2ImageUrl = tail.getSummonerSpellImageUrl(from.summonerSpell2.iconName),
    )

    private fun mapPlayer(
        from: Participant,
        accountLoadTrigger: (String) -> Unit,
        summonerLoadTrigger: (String) -> Unit,
    ): PlayerInfo = when {
        from.isBot -> BotPlayerInfo
        from.puuid == null -> UnknownPlayerInfo
        else -> {
            accountLoadTrigger(from.puuid)
            summonerLoadTrigger(from.puuid)
            KnownPlayerInfo(
                puuid = from.puuid,
                account = LoadablePart.Loading,
                summoner = LoadablePart.Loading,
            )
        }
    }

    fun interface AccountLoadTrigger {
        operator fun invoke(teamId: Int, puuid: String)
    }

    fun interface SummonerLoadTrigger {
        operator fun invoke(teamId: Int, puuid: String)
    }

    private companion object {
        const val BLUE_TEAM_ID = 100
    }
}
