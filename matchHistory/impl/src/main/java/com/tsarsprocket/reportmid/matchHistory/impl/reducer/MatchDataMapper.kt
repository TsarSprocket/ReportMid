package com.tsarsprocket.reportmid.matchHistory.impl.reducer

import com.tsarsprocket.reportmid.dataDragonApi.data.DataDragon
import com.tsarsprocket.reportmid.lol.api.model.ITEM_ID_EMPTY
import com.tsarsprocket.reportmid.matchHistory.impl.domain.model.MatchData
import com.tsarsprocket.reportmid.matchHistory.impl.domain.model.PlayerData
import com.tsarsprocket.reportmid.matchHistory.impl.domain.model.TeamData
import com.tsarsprocket.reportmid.matchHistory.impl.viewState.ChampionInfo
import com.tsarsprocket.reportmid.matchHistory.impl.viewState.GameOutcome
import com.tsarsprocket.reportmid.matchHistory.impl.viewState.GameOutcome.LOSE
import com.tsarsprocket.reportmid.matchHistory.impl.viewState.GameOutcome.REMAKE
import com.tsarsprocket.reportmid.matchHistory.impl.viewState.GameOutcome.WIN
import com.tsarsprocket.reportmid.matchHistory.impl.viewState.ItemInfo
import com.tsarsprocket.reportmid.matchHistory.impl.viewState.MatchInfo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import javax.inject.Inject
import kotlin.math.floor
import kotlin.math.sqrt

internal class MatchDataMapper @Inject constructor(
    dataDragon: DataDragon,
) {

    private val tail by lazy { dataDragon.tail }

    fun map(from: MatchData): MatchInfo {
        val isSummonerRift = from.gameType.isSummonerRift

        return MatchInfo(
            gameOutcome = mapOutcome(from),
            self = mapSelf(from),
            gameType = from.gameType.name,
            isSummonerRift = isSummonerRift,
            kills = from.me.kills.toString(),
            deaths = from.me.deaths.toString(),
            assists = from.me.assists.toString(),
            items = mapItems(from.me.items),
            ward = mapSingleItem(from.me.ward),
            teams = mapTeams(from.teams, isSummonerRift),
        )
    }

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

    /**
     * Prepares team layout in square block assuming columns containing rows. It tries to make it more tall than wide.
     * If the list is shorter than the number of cells in the grid, then the remaining columns are shorter
     * @param isReversed reverses column order
     */
    private fun mapTeamSquared(players: List<PlayerData>, isReversed: Boolean): ImmutableList<ImmutableList<ChampionInfo>> {
        val width = floor(sqrt(players.size.toFloat())).toInt()
        val height = players.size / width + if(players.size % width > 0) 1 else 0

        val grid = Array(width) { Array<ChampionInfo?>(height) { null } }

        players.forEachIndexed { index, player ->
            val column = (index % width).let { if(isReversed) width - it - 1 else it }
            grid[column][index / width] = player.toChampionInfo()
        }

        return grid.map { column -> column.mapNotNull { it }.toPersistentList() }.toPersistentList()
    }

    /**
     * Just layouts teams sequentially in square blocks
     */
    private fun mapTeamsDefault(teams: List<TeamData>): ImmutableList<ImmutableList<ImmutableList<ChampionInfo>>> {
        return teams.map { team -> mapTeamSquared(team.players, isReversed = false) }.toPersistentList()
    }

    private fun mapTeams(teams: List<TeamData>, isSummonerRift: Boolean): ImmutableList<ImmutableList<ImmutableList<ChampionInfo?>>> {
        return when {
            isSummonerRift -> mapTeamsSummonerRift(teams)
            teams.size == 2 -> mapTwoTeamsMirrored(teams.first().players, teams.last().players)
            else -> mapTeamsDefault(teams)
        }
    }

    /**
     * Maps teams for the summoner rift only. First team (blue) -
     * (0)
     * (1) (2)
     * ( ) (3) (4)+
     * Second team (red) -
     * (0) (1)
     * ( ) (2) (3)
     * ( ) ( ) (4)+
     * In theory, there should exactly 2 teams, 5 players in each
     */
    private fun mapTeamsSummonerRift(teams: List<TeamData>): ImmutableList<ImmutableList<ImmutableList<ChampionInfo?>>> {
        val blueTeam = teams.getOrNull(1)?.players?.let { players ->
            listOfNotNull(
                listOfNotNull(
                    players.getOrNull(0)?.toChampionInfo(),
                    players.getOrNull(1)?.toChampionInfo(),
                ).takeIf { it.isNotEmpty() }?.toPersistentList(),

                (listOf(null) + listOfNotNull(
                    players.getOrNull(2)?.toChampionInfo(),
                    players.getOrNull(3)?.toChampionInfo()
                )).toPersistentList(),

                (listOf(null, null) + players.drop(4).map { it.toChampionInfo() }).toPersistentList()
            ).takeIf { it.isNotEmpty() }?.toPersistentList()
        }

        val redTeam = teams.getOrNull(0)?.players?.let { players ->
            listOfNotNull(
                listOfNotNull(
                    players.getOrNull(0)?.toChampionInfo(),
                ).takeIf { it.isNotEmpty() }?.toPersistentList(),

                listOfNotNull(
                    players.getOrNull(1)?.toChampionInfo(),
                    players.getOrNull(2)?.toChampionInfo()
                ).takeIf { it.isNotEmpty() }?.toPersistentList(),

                (listOf(null) + players.drop(3).map { it.toChampionInfo() }).takeIf { it.isNotEmpty() }?.toPersistentList()
            ).takeIf { it.isNotEmpty() }?.toPersistentList()
        }

        return listOfNotNull(blueTeam, redTeam).toPersistentList()
    }

    /**
     * For ARAM etc teams should be shown face-to-face, i.e. the column order of the first one should be mirrored
     */
    private fun mapTwoTeamsMirrored(firstTeam: List<PlayerData>, secondTeam: List<PlayerData>): ImmutableList<ImmutableList<ImmutableList<ChampionInfo?>>> {
        return persistentListOf(mapTeamSquared(firstTeam, isReversed = true), mapTeamSquared(firstTeam, isReversed = false))
    }

    private fun mapSingleItem(itemId: Int?): ItemInfo {
        return if(itemId != null && itemId != ITEM_ID_EMPTY) {
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
    }

    private fun PlayerData.toChampionInfo(): ChampionInfo = ChampionInfo(
        icon = championIcon,
        name = championName
    )

    private companion object {
        private const val COLUMNS_IN_ROW = 3
    }
}