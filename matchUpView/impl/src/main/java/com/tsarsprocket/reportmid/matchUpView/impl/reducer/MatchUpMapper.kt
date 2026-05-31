package com.tsarsprocket.reportmid.matchUpView.impl.reducer

import android.content.Context
import com.tsarsprocket.reportmid.baseApi.di.AppContext
import com.tsarsprocket.reportmid.dataDragonApi.data.DataDragon
import com.tsarsprocket.reportmid.lol.api.domain.model.Region
import com.tsarsprocket.reportmid.matchUpView.impl.R
import com.tsarsprocket.reportmid.matchUpView.impl.domain.model.CurrentMatchUp
import com.tsarsprocket.reportmid.matchUpView.impl.domain.model.Participant
import com.tsarsprocket.reportmid.matchUpView.impl.domain.model.Team
import com.tsarsprocket.reportmid.matchUpView.impl.viewState.AccountInfo
import com.tsarsprocket.reportmid.matchUpView.impl.viewState.MatchUpState
import com.tsarsprocket.reportmid.matchUpView.impl.viewState.ParticipantInfo
import com.tsarsprocket.reportmid.matchUpView.impl.viewState.TeamInfo
import com.tsarsprocket.reportmid.viewStateApi.viewState.LoadablePart
import javax.inject.Inject

internal class MatchUpMapper @Inject constructor(
    dataDragon: DataDragon,
    @param:AppContext
    private val appContext: Context,
) {

    private val tail by lazy { dataDragon.tail }

    fun map(from: CurrentMatchUp, puuid: String, region: Region, accountLoadTrigger: AccountLoadTrigger): MatchUpState {
        return MatchUpState(
            puuid = puuid,
            region = region,
            teams = from.teams.associate { it.id to mapTeam(it, accountLoadTrigger) },
        )
    }

    private fun mapTeam(from: Team, accountLoadTrigger: AccountLoadTrigger): TeamInfo {
        return TeamInfo(
            id = from.id,
            isBlueSide = from.id == BLUE_TEAM_ID,
            participants = from.participants.associate { it.puuid to mapParticipant(it) { puuid -> accountLoadTrigger(from.id, puuid) } },
        )
    }

    private fun mapParticipant(from: Participant, accountLoadTrigger: (String) -> Unit): ParticipantInfo {
        return ParticipantInfo(
            puuid = from.puuid,
            championImageUrl = tail.getChampionImageUrl(from.champion.iconName),
            account = if (from.isBot) {
                LoadablePart.Loaded(AccountInfo(appContext.getString(R.string.match_up_view_bot_player_name)))
            } else {
                accountLoadTrigger(from.puuid)
                LoadablePart.Loading
            },
            primaryRuneImageUrls = from.runes.primaryRunes.map { tail.getRuneImageUrl(it) },
            secondaryRuneImageUrls = from.runes.secondaryRunes.map { tail.getRuneImageUrl(it) },
            summonerSpell1ImageUrl = tail.getSummonerSpellImageUrl(from.summonerSpell1.iconName),
            summonerSpell2ImageUrl = tail.getSummonerSpellImageUrl(from.summonerSpell2.iconName),
        )
    }

    fun interface AccountLoadTrigger {
        operator fun invoke(teamId: Int, puuid: String)
    }

    private companion object {
        const val BLUE_TEAM_ID = 100
    }
}
