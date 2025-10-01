package com.tsarsprocket.reportmid.matchHistory.impl.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.tsarsprocket.reportmid.lol.api.model.Region
import com.tsarsprocket.reportmid.matchHistory.impl.viewState.ChampionInfo
import com.tsarsprocket.reportmid.matchHistory.impl.viewState.GameOutcome
import com.tsarsprocket.reportmid.matchHistory.impl.viewState.ItemInfo
import com.tsarsprocket.reportmid.matchHistory.impl.viewState.LoadingMoreItem
import com.tsarsprocket.reportmid.matchHistory.impl.viewState.MatchInfo
import com.tsarsprocket.reportmid.matchHistory.impl.viewState.ShowingMatchHistoryState
import com.tsarsprocket.reportmid.theme.ReportMidSpecialColors
import com.tsarsprocket.reportmid.theme.ReportMidTheme
import com.tsarsprocket.reportmid.theme.reportMidTypography
import com.tsarsprocket.reportmid.utils.common.EMPTY_STRING
import com.tsarsprocket.reportmid.utils.compose.Failure
import com.tsarsprocket.reportmid.utils.compose.SkeletonRectangle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import com.tsarsprocket.reportmid.resLib.R as ResLib

private const val GAME_OVERVIEW_SPACING = 8
private const val ITEM_ICON_SIZE = 18
private const val ITEM_SPACING = 2
private const val PLAYER_ICON_SIZE = 40
private const val SPACING_AROUND_SLASH = 4
private const val TEAMMATE_ICON_SIZE = 18
private const val TEAMS_SPACING = 2
private const val TEAMS_INTERSPACING = 8
private const val WIN_INDICATOR_WIDTH = 16

@Composable
internal fun MatchHistory(
    modifier: Modifier,
    state: ShowingMatchHistoryState,
    onMoreToShow: () -> Unit,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(state.itemsInList) { index ->
            when(val item = state.getItemToShow(index)) {
                is MatchInfo -> MatchItem(item)
                LoadingMoreItem -> if(state.canLoadMore) ShowLoadingMoreItem(state.itemsInList, state.isLoading, onMoreToShow)
            }
        }
    }
}

@Composable
private fun MatchItem(item: MatchInfo) {
    val backgroundColor = when(item.gameOutcome) {
        GameOutcome.WIN -> ReportMidSpecialColors.win
        GameOutcome.LOSE -> ReportMidSpecialColors.lose
        GameOutcome.REMAKE -> ReportMidSpecialColors.remake
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor.compositeOver(CardDefaults.cardColors().containerColor)
        )
    ) {
        with(item) {
            Row(
                modifier = Modifier
                    .height(IntrinsicSize.Max)
                    .padding(horizontal = 24.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                PlayerChampion(info = self)

                GameOverview(
                    gameType = gameType,
                    kills = kills,
                    deaths = deaths,
                    assists = assists,
                )

                PlayerItems(
                    items = items,
                    ward = ward,
                )

                Teams(
                    teammates = teammates,
                    enemies = enemies,
                )
            }
        }
    }
}

@Composable
private fun GameItem(modifier: Modifier, item: ItemInfo) {
    when(item) {
        is ItemInfo.Known -> {
            AsyncImage(
                modifier = modifier,
                model = item.icon,
                contentDescription = item.name,
            )
        }

        is ItemInfo.Unknown -> {
            SkeletonRectangle(
                modifier = modifier,
                cornerSize = 1.dp
            )
        }

        is ItemInfo.Empty -> {
            Spacer(modifier)
        }
    }
}

@Composable
fun GameOverview(gameType: String, kills: String, deaths: String, assists: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val textStyle = reportMidTypography.bodyMedium

        Text(
            text = gameType,
            style = textStyle,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(GAME_OVERVIEW_SPACING.dp))

        Row {
            Text(
                text = kills,
                style = textStyle,
            )

            Text(
                modifier = Modifier.padding(horizontal = SPACING_AROUND_SLASH.dp),
                text = "/",
                style = textStyle,
            )

            Text(
                text = deaths,
                style = textStyle,
            )

            Text(
                modifier = Modifier.padding(horizontal = SPACING_AROUND_SLASH.dp),
                text = "/",
                style = textStyle,
            )

            Text(
                text = assists,
                style = textStyle,
            )
        }
    }
}

@Composable
private fun Teams(teammates: ImmutableList<ImmutableList<ChampionInfo>>, enemies: ImmutableList<ImmutableList<ChampionInfo>>) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(TEAMS_INTERSPACING.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        listOf(teammates, enemies).forEach { team ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(TEAMS_SPACING.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                team.forEach { teamColumn ->
                    Column(
                        verticalArrangement = Arrangement.spacedBy(TEAMS_SPACING.dp),
                    ) {
                        teamColumn.forEach { player ->
                            SubcomposeAsyncImage(
                                modifier = Modifier.size(TEAMMATE_ICON_SIZE.dp),
                                model = player.icon,
                                contentDescription = player.name,
                                loading = {
                                    SkeletonRectangle(
                                        modifier = Modifier.size(TEAMMATE_ICON_SIZE.dp),
                                        cornerSize = 1.dp
                                    )
                                },
                                error = {
                                    Failure(
                                        modifier = Modifier.size(TEAMMATE_ICON_SIZE.dp),
                                        iconPainter = painterResource(ResLib.drawable.ic_failure),
                                        iconSize = TEAMMATE_ICON_SIZE.dp * 0.66f,
                                        description = player.name,
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PlayerChampion(info: ChampionInfo) {
    AsyncImage(
        modifier = Modifier.size(PLAYER_ICON_SIZE.dp),
        model = info.icon,
        contentDescription = info.name,
    )
}

@Composable
private fun PlayerItems(items: ImmutableList<ImmutableList<ItemInfo>>, ward: ItemInfo) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(ITEM_SPACING.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(ITEM_SPACING.dp),
        ) {
            items.forEach { row ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(ITEM_SPACING.dp),
                ) {
                    row.forEach { item -> GameItem(Modifier.size(ITEM_ICON_SIZE.dp), item) }
                }
            }
        }

        GameItem(Modifier.size(ITEM_ICON_SIZE.dp), ward)
    }
}

@Composable
private fun ShowLoadingMoreItem(itemsInList: Int, isLoading: Boolean, onMoreToShow: () -> Unit) {
    LaunchedEffect(itemsInList) {
        if(!isLoading) onMoreToShow()
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .padding(16.dp)
                .size(32.dp)
                .align(Alignment.Center),
        )
    }
}

//  Preview ///////////////////////////////////////////////////////////////////

@Preview
@Composable
fun MatchHistoryPreview() {
    ReportMidTheme {
        MatchHistory(
            modifier = Modifier,
            state = ShowingMatchHistoryState(
                puuid = EMPTY_STRING,
                region = Region.EUROPE_WEST,
                matches = persistentListOf(
                    MatchInfo(
                        GameOutcome.WIN,
                        self = ChampionInfo(
                            icon = EMPTY_STRING,
                            name = "Malzahar",
                        ),
                        gameType = "NORMAL DRAFT",
                        kills = "0",
                        deaths = "3",
                        assists = "15",
                        items = persistentListOf(),
                        ward = ItemInfo.Known(
                            icon = EMPTY_STRING,
                            name = EMPTY_STRING,
                        ),
                        teammates = persistentListOf(),
                        enemies = persistentListOf(),
                    ),
                    MatchInfo(
                        GameOutcome.LOSE,
                        self = ChampionInfo(
                            icon = EMPTY_STRING,
                            name = "Malzahar",
                        ),
                        gameType = "NORMAL RANKED",
                        kills = "3",
                        deaths = "1",
                        assists = "5",
                        items = persistentListOf(),
                        ward = ItemInfo.Known(
                            icon = EMPTY_STRING,
                            name = EMPTY_STRING,
                        ),
                        teammates = persistentListOf(),
                        enemies = persistentListOf(),
                    )
                ),
                isLoading = false,
                canLoadMore = true,
            ),
            onMoreToShow = {},
        )
    }
}