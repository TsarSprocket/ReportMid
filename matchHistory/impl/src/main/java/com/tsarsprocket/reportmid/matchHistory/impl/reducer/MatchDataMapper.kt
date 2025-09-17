package com.tsarsprocket.reportmid.matchHistory.impl.reducer

import com.tsarsprocket.reportmid.dataDragonApi.data.DataDragon
import com.tsarsprocket.reportmid.matchHistory.impl.domain.model.MatchData
import com.tsarsprocket.reportmid.matchHistory.impl.domain.model.PlayerData
import com.tsarsprocket.reportmid.matchHistory.impl.viewState.ChampionInfo
import com.tsarsprocket.reportmid.matchHistory.impl.viewState.GameOutcome
import com.tsarsprocket.reportmid.matchHistory.impl.viewState.GameOutcome.LOSE
import com.tsarsprocket.reportmid.matchHistory.impl.viewState.GameOutcome.REMAKE
import com.tsarsprocket.reportmid.matchHistory.impl.viewState.GameOutcome.WIN
import com.tsarsprocket.reportmid.matchHistory.impl.viewState.ItemInfo
import com.tsarsprocket.reportmid.matchHistory.impl.viewState.MatchInfo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import javax.inject.Inject

internal class MatchDataMapper @Inject constructor(
    dataDragon: DataDragon,
) {

    private val tail by lazy { dataDragon.tail }

    fun map(from: MatchData): MatchInfo = MatchInfo(
        gameOutcome = mapOutcome(from),
        self = mapSelf(from),
        gameType = from.gameType.name,
        kills = from.me.kills.toString(),
        deaths = from.me.deaths.toString(),
        assists = from.me.assists.toString(),
        items = mapItems(from.me.items),
        ward = mapSingleItem(from.me.ward),
        teammates = mapTeamPlayers(from.myTeam.players, from.me.puuid),
        enemies = mapTeamPlayers(from.enemyTeam.players, from.me.puuid),
    )

    private fun mapItems(items: List<Int?>): ImmutableList<ImmutableList<ItemInfo>> {
        return items.chunked(COLUMNS_IN_ROW).map {
            it.map(::mapSingleItem).toPersistentList()
        }.toPersistentList()
    }

    private fun mapOutcome(matchData: MatchData): GameOutcome = when {
        matchData.isRemake -> REMAKE
        matchData.isWin -> WIN
        else -> LOSE
    }

    private fun mapSelf(matchData: MatchData): ChampionInfo = with(matchData.me) {
        ChampionInfo(
            icon = championIcon,
            name = championName,
        )
    }

    private fun mapTeamPlayers(data: List<PlayerData>, myPuuid: String): ImmutableList<ChampionInfo> {
        return data.filter { it.puuid != myPuuid }.map {
            ChampionInfo(
                icon = it.championIcon,
                name = it.championName,
            )
        }.toPersistentList()
    }

    private fun mapSingleItem(itemId: Int?): ItemInfo = if(itemId != null) {
        try {
            tail.getItemById(itemId).let { item ->
                ItemInfo.Known(
                    icon = tail.getItemImageUrl(item),
                    name = item.name,
                )
            }
        } catch(_: Exception) {
            ItemInfo.Unknown
        }
    } else {
        ItemInfo.Empty
    }

    private companion object {
        private const val COLUMNS_IN_ROW = 3
    }
}