package com.tsarsprocket.reportmid.matchUpView.impl.domain

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.currentGameData.api.data.model.CurrentGame
import com.tsarsprocket.reportmid.currentGameData.api.data.model.CurrentGameParticipant
import com.tsarsprocket.reportmid.currentGameData.api.data.model.CurrentTeam
import com.tsarsprocket.reportmid.dataDragonApi.data.DataDragon
import com.tsarsprocket.reportmid.matchUpView.impl.domain.model.CurrentMatchUp
import com.tsarsprocket.reportmid.matchUpView.impl.domain.model.Participant
import com.tsarsprocket.reportmid.matchUpView.impl.domain.model.Team
import javax.inject.Inject
import kotlin.time.ExperimentalTime


@PerApi
internal class DomainMapper @Inject constructor(
    dataDragon: DataDragon,
) {

    private val tail by lazy { dataDragon.tail }

    @OptIn(ExperimentalTime::class)
    fun map(currentGame: CurrentGame.InProgress): CurrentMatchUp {
        return CurrentMatchUp(
            gameType = currentGame.gameType,
            gameStartTime = currentGame.gameStartTime,
            teams = currentGame.teams.map { mapTeam(it) },
        )
    }

    private fun mapTeam(teams: CurrentTeam): Team {
        return Team(
            participants = teams.participants.map { mapParticipant(it) },
            bannedChampionIds = teams.bannedChampionIds,
        )
    }

    private fun mapParticipant(participant: CurrentGameParticipant): Participant = with(participant) {
        Participant(
            puuid = puuid,
            champion = tail.getChampionById(championId),
            profileIcon = profileIcon,
            isBot = isBot,
            runes = runes,
            summonerSpell1 = summonerSpell1,
            summonerSpell2 = summonerSpell2,
        )
    }
}