package com.tsarsprocket.reportmid.matchHistory.impl.view

import androidx.compose.foundation.border
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
import androidx.compose.ui.res.stringResource
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
import com.tsarsprocket.reportmid.theme.reportMidColorScheme
import com.tsarsprocket.reportmid.theme.reportMidTypography
import com.tsarsprocket.reportmid.utils.common.EMPTY_STRING
import com.tsarsprocket.reportmid.utils.compose.Failure
import com.tsarsprocket.reportmid.utils.compose.SkeletonRectangle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import com.tsarsprocket.reportmid.lol.api.R as RLolApi
import com.tsarsprocket.reportmid.resLib.R as ResLib

private const val ITEM_ICON_SIZE = 18
private const val ITEM_SPACING = 4
private const val PLAYER_ICON_SIZE = 40
private const val SPACING_AROUND_SLASH = 4
private const val TEAMMATE_ICON_SIZE = 18
private const val TEAMS_SPACING = 2
private const val TEAMS_HORIZONTAL_INTERSPACING = 4
private const val TEAMS_VERTICAL_INTERSPACING = 6

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
                    outcome = gameOutcome,
                    kills = kills,
                    deaths = deaths,
                    assists = assists,
                )

                PlayerItems(
                    items = items,
                    ward = ward,
                )

                if(isSummonerRift) {
                    SummonerRiftTeams(teams)
                } else {
                    GeneralTeams(teams)
                }
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
            Box(
                modifier = modifier
                    .border(width = 1.dp, color = reportMidColorScheme.secondary)
            )
        }
    }
}

@Composable
fun GameOverview(gameType: String, kills: String, deaths: String, assists: String, outcome: GameOutcome) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {
        val textStyle = reportMidTypography.bodyMedium

        Text(
            text = gameType,
            style = textStyle,
            textAlign = TextAlign.Center,
        )

        val outcomeText = when(outcome) {
            GameOutcome.WIN -> RLolApi.string.lol_api_game_outcome_win
            GameOutcome.LOSE -> RLolApi.string.lol_api_game_outcome_defeat
            GameOutcome.REMAKE -> RLolApi.string.lol_api_game_outcome_remake
        }

        Text(
            text = stringResource(outcomeText),
            style = textStyle,
        )

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
private fun GeneralTeams(teams: ImmutableList<ImmutableList<ImmutableList<ChampionInfo?>>>) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(TEAMS_HORIZONTAL_INTERSPACING.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        teams.forEach { team ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(TEAMS_SPACING.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                team.forEach { teamColumn ->
                    Column(
                        verticalArrangement = Arrangement.spacedBy(TEAMS_SPACING.dp),
                    ) {
                        teamColumn.forEach { player ->
                            SinglePlayer(
                                modifier = Modifier.size(TEAMMATE_ICON_SIZE.dp),
                                player = player,
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
        verticalAlignment = Alignment.CenterVertically,
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

@Composable
private fun SinglePlayer(
    modifier: Modifier,
    player: ChampionInfo?
) {
    if(player != null) {
        SubcomposeAsyncImage(
            modifier = modifier,
            model = player.icon,
            contentDescription = player.name,
            loading = {
                SkeletonRectangle(
                    modifier = modifier,
                    cornerSize = 1.dp
                )
            },
            error = {
                Failure(
                    modifier = modifier,
                    iconPainter = painterResource(ResLib.drawable.ic_failure),
                    iconSize = TEAMMATE_ICON_SIZE.dp * 0.66f,
                    description = player.name,
                )
            }
        )
    } else {
        Spacer(modifier)
    }
}

@Composable
private fun SingleSummonerRiftTeam(modifier: Modifier, team: ImmutableList<ImmutableList<ChampionInfo?>>) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(TEAMS_SPACING.dp),
    ) {
        team.forEach { teamColumn ->
            Column(
                verticalArrangement = Arrangement.spacedBy(TEAMS_SPACING.dp),
            ) {
                teamColumn.forEach { player ->
                    SinglePlayer(
                        modifier = Modifier.size(TEAMMATE_ICON_SIZE.dp),
                        player = player,
                    )
                }
            }
        }
    }
}

@Composable
private fun SummonerRiftTeams(teams: ImmutableList<ImmutableList<ImmutableList<ChampionInfo?>>>) {
    Box {
        // Blue team
        teams.forEachIndexed { index, team ->
            SingleSummonerRiftTeam(
                modifier = Modifier
                    .padding(
                        start = ((TEAMMATE_ICON_SIZE + TEAMS_SPACING + TEAMS_HORIZONTAL_INTERSPACING) * index).dp,
                        top = (TEAMS_VERTICAL_INTERSPACING * (teams.size - index - 1)).dp,
                    ),
                team = team,
            )
        }
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
                        isSummonerRift = false,
                        kills = "0",
                        deaths = "3",
                        assists = "15",
                        items = persistentListOf(),
                        ward = ItemInfo.Known(
                            icon = EMPTY_STRING,
                            name = EMPTY_STRING,
                        ),
                        teams = persistentListOf(),
                    ),
                    MatchInfo(
                        GameOutcome.LOSE,
                        self = ChampionInfo(
                            icon = EMPTY_STRING,
                            name = "Malzahar",
                        ),
                        gameType = "NORMAL RANKED",
                        isSummonerRift = false,
                        kills = "3",
                        deaths = "1",
                        assists = "5",
                        items = persistentListOf(),
                        ward = ItemInfo.Known(
                            icon = EMPTY_STRING,
                            name = EMPTY_STRING,
                        ),
                        teams = persistentListOf(),
                    )
                ),
                isLoading = false,
                canLoadMore = true,
            ),
            onMoreToShow = {},
        )
    }
}