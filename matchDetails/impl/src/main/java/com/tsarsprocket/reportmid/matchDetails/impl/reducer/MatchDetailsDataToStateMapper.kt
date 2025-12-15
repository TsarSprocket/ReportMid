package com.tsarsprocket.reportmid.matchDetails.impl.reducer

import android.content.Context
import android.text.format.DateUtils
import com.tsarsprocket.reportmid.baseApi.di.AppContext
import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.dataDragonApi.data.DataDragon
import com.tsarsprocket.reportmid.lol.api.domain.model.Rune
import com.tsarsprocket.reportmid.lol.api.domain.model.RuneStyle
import com.tsarsprocket.reportmid.lol.api.presentation.ItemInfoMapper
import com.tsarsprocket.reportmid.lol.api.presentation.SummonerSpellInfoMapper
import com.tsarsprocket.reportmid.matchDetails.impl.R
import com.tsarsprocket.reportmid.matchDetails.impl.domain.model.GameOutcome
import com.tsarsprocket.reportmid.matchDetails.impl.domain.model.GameOutcome.DEFEAT
import com.tsarsprocket.reportmid.matchDetails.impl.domain.model.GameOutcome.REMAKE
import com.tsarsprocket.reportmid.matchDetails.impl.domain.model.GameOutcome.WIN
import com.tsarsprocket.reportmid.matchDetails.impl.domain.model.MatchDetailsData
import com.tsarsprocket.reportmid.matchDetails.impl.domain.model.PlayerData
import com.tsarsprocket.reportmid.matchDetails.impl.domain.model.TeamData
import com.tsarsprocket.reportmid.matchDetails.impl.viewState.MatchDetailsState
import com.tsarsprocket.reportmid.matchDetails.impl.viewState.PlayerInfo
import com.tsarsprocket.reportmid.matchDetails.impl.viewState.RuneInfo
import com.tsarsprocket.reportmid.matchDetails.impl.viewState.RuneSetInfo
import com.tsarsprocket.reportmid.matchDetails.impl.viewState.TeamInfo
import kotlinx.collections.immutable.toPersistentList
import javax.inject.Inject

@PerApi
internal class MatchDetailsDataToStateMapper @Inject constructor(
    private val itemInfoMapper: ItemInfoMapper,
    private val summonerSpellInfoMapper: SummonerSpellInfoMapper,
    private val dataDragon: DataDragon,
    @param:AppContext
    private val context: Context,
) {

    private val dragonTail
        get() = dataDragon.tail

    fun map(data: MatchDetailsData): MatchDetailsState = MatchDetailsState(
        region = data.region,
        gameType = data.gameType.name,
        duration = formatGameDuration(data.duration),
        teams = data.teams.map { teamData -> mapTeam(teamData) }.toPersistentList()
    )

    private fun mapTeam(teamData: TeamData): TeamInfo = TeamInfo(
        gameOutcome = getGameOutcome(teamData.gameOutcome),
        teamSide = teamData.teamSide,
        players = teamData.players.map { player -> mapPlayer(player) }.toPersistentList()
    )

    private fun mapPlayer(data: PlayerData): PlayerInfo {
        val champion = dragonTail.getChampionById(data.championId)

        return PlayerInfo(
            puuid = data.puuid,
            nameWithTag = "${data.gameName}#${data.tagLine}",
            summonerLevel = data.summonerLevel,
            championIcon = dragonTail.getChampionImageUrl(champion.iconName),
            championName = champion.name,
            championLevel = data.championLevel,
            kills = data.kills,
            deaths = data.deaths,
            assists = data.assists,
            runes = getRuneSetInfo(data.primaryRunes, data.secondaryRunes),
            summonerSpells = data.summonerSpells.map { summonerSpell -> summonerSpellInfoMapper.map(summonerSpell) }.toPersistentList(),
            items = data.items.map { item -> itemInfoMapper.map(item) }.toPersistentList(),
            ward = itemInfoMapper.map(data.ward),
        )
    }

    private fun getGameOutcome(gameOutcome: GameOutcome): String = when(gameOutcome) {
        DEFEAT -> context.getString(R.string.match_details_game_outcome_defeat)
        REMAKE -> context.getString(R.string.match_details_game_outcome_remake)
        WIN -> context.getString(R.string.match_details_game_outcome_win)
    }

    private fun getRuneSetInfo(primaryRunes: RuneStyle, secondaryRunes: RuneStyle): RuneSetInfo {
        return RuneSetInfo(
            primaryRune = primaryRunes.runes.first().run { toRuneInfo() },
            primaryPath = primaryRunes.runes.drop(1).map { it.toRuneInfo() }.toPersistentList(),
            secondaryPath = secondaryRunes.runes.map { it.toRuneInfo() }.toPersistentList()
        )
    }

    fun formatGameDuration(duration: Long): String = DateUtils.formatElapsedTime(duration)

    fun Rune.toRuneInfo(): RuneInfo = RuneInfo(
        icon = dragonTail.getRuneImageUrl(this),
        name = this.name
    )
}