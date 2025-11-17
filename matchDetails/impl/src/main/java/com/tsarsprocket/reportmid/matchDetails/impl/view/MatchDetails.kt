package com.tsarsprocket.reportmid.matchDetails.impl.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tsarsprocket.reportmid.lol.api.model.Region
import com.tsarsprocket.reportmid.lol.api.presentation_model.ItemInfo
import com.tsarsprocket.reportmid.matchDetails.impl.viewState.MatchDetailsState
import com.tsarsprocket.reportmid.matchDetails.impl.viewState.PlayerInfo
import com.tsarsprocket.reportmid.matchDetails.impl.viewState.RuneInfo
import com.tsarsprocket.reportmid.matchDetails.impl.viewState.RuneSetInfo
import com.tsarsprocket.reportmid.matchDetails.impl.viewState.SummonerSpellInfo
import com.tsarsprocket.reportmid.matchDetails.impl.viewState.TeamInfo
import com.tsarsprocket.reportmid.matchDetails.impl.viewState.TeamSide
import com.tsarsprocket.reportmid.theme.ReportMidSpecialColors
import com.tsarsprocket.reportmid.theme.ReportMidTheme
import com.tsarsprocket.reportmid.theme.reportMidColorScheme
import com.tsarsprocket.reportmid.theme.reportMidTypography
import com.tsarsprocket.reportmid.utils.common.EMPTY_STRING
import com.tsarsprocket.reportmid.utils.compose.Failure
import com.tsarsprocket.reportmid.utils.compose.ReloadableImage
import com.tsarsprocket.reportmid.utils.compose.SkeletonRectangle
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import com.tsarsprocket.reportmid.resLib.R as ResLibR


private const val CHAMPION_ICON_SIZE = 48
private const val DIVIDER_HORIZONTAL_PADDING = 16
private const val ITEM_SIZE = 12
private const val ITEMS_SPACING = 2
private const val NUMBER_OF_ITEM_COLUMNS = 3
private const val PRIMARY_RUNE_SIZE = 12
private const val RUNE_SPACING = 2
private const val SECONDARY_RUNE_SIZE = 8
private const val SUMMONER_SPELL_SIZE = 12

@Composable
internal fun MatchDetails(
    modifier: Modifier,
    state: MatchDetailsState,
    onPlayerClick: (String) -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = state.gameType,
                style = reportMidTypography.headlineSmall,
            )

            Text(
                text = state.duration,
                style = reportMidTypography.headlineSmall,
            )
        }

        state.teams.forEach { teamInfo ->

            TeamDetails(teamInfo, onPlayerClick)
        }
    }
}

@Composable
private fun TeamDetails(teamInfo: TeamInfo, onPlayerClick: (String) -> Unit) {
    val backgroundColour = when(teamInfo.teamSide) {
        TeamSide.Blue -> ReportMidSpecialColors.blueTeam
        TeamSide.Red -> ReportMidSpecialColors.redTeam
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColour)
            .padding(bottom = 8.dp),
    ) {
        Text(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            text = teamInfo.gameOutcome,
            style = reportMidTypography.bodyLarge,
            textAlign = TextAlign.Center,
        )

        teamInfo.players.forEachIndexed { index, playerInfo ->
            PlayerDetails(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 16.dp),
                playerInfo = playerInfo,
                onClick = onPlayerClick,
            )

            if(index < teamInfo.players.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = DIVIDER_HORIZONTAL_PADDING.dp),
                    color = reportMidColorScheme.surface,
                )
            }
        }
    }
}

@Composable
private fun PlayerDetails(modifier: Modifier, playerInfo: PlayerInfo, onClick: (String) -> Unit) {
    Row(
        modifier = modifier.clickable { onClick(playerInfo.puuid) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Absolute.SpaceBetween
    ) {
        ChampionIcon(playerInfo.championIcon, playerInfo.championName)

        ChampLevelAndSummoners(playerInfo.championLevel, playerInfo.summonerSpells)

        Runes(playerInfo.runes)

        KDAAndStats(playerInfo)

        ItemsAndWard(playerInfo.items, playerInfo.ward)
    }
}

@Composable
private fun ItemsAndWard(items: List<ItemInfo>, ward: ItemInfo) {
    val regroupedIntoColumns = (0 until NUMBER_OF_ITEM_COLUMNS).map { colNo -> items.filterIndexed { i, _ -> i % NUMBER_OF_ITEM_COLUMNS == colNo } }

    Row(
        horizontalArrangement = Arrangement.spacedBy(ITEMS_SPACING.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        regroupedIntoColumns.forEach { itemColumn ->
            Column(
                verticalArrangement = Arrangement.spacedBy(ITEMS_SPACING.dp)
            ) {
                itemColumn.forEach { item ->
                    ItemIcon(
                        modifier = Modifier.size(ITEM_SIZE.dp),
                        item = item,
                    )
                }
            }
        }

        ItemIcon(
            modifier = Modifier.size(ITEM_SIZE.dp),
            item = ward,
        )
    }
}

@Composable
fun ItemIcon(modifier: Modifier, item: ItemInfo) {
    when(item) {
        ItemInfo.Empty -> Box(
            modifier = modifier
                .background(Color.Black)
                .border(width = 1.dp, color = reportMidColorScheme.primary)
        )

        is ItemInfo.Known -> ReloadableImage(
            modifier = modifier,
            url = item.icon,
            contentDescription = item.name,
            loading = { modifier, _ ->
                SkeletonRectangle(modifier = modifier)
            },
            error = { modifier, _, onClick ->
                Failure(
                    modifier = modifier,
                    iconPainter = painterResource(ResLibR.drawable.ic_failure),
                    description = item.name,
                    onClick = onClick,
                )
            }
        )

        ItemInfo.Unknown -> Failure(
            modifier = modifier,
            iconPainter = painterResource(ResLibR.drawable.ic_placeholder),
        )
    }
}

@Composable
private fun ChampionIcon(url: String, name: String) {
    val modifier = Modifier.size(CHAMPION_ICON_SIZE.dp)

    ReloadableImage(
        modifier = modifier,
        url = url,
        contentDescription = name,
        loading = { modifier, _ ->
            SkeletonRectangle(
                modifier = modifier,
                cornerSize = 1.dp,
            )
        },
        error = { modifier, _, onClick ->
            Failure(
                modifier = modifier,
                iconPainter = painterResource(ResLibR.drawable.ic_failure),
                description = name,
                onClick = onClick,
            )
        }
    )
}

@Composable
private fun ChampLevelAndSummoners(championLevel: Int, summonerSpells: List<SummonerSpellInfo>) {
    Column {
        Text(
            text = championLevel.toString(),
            style = reportMidTypography.bodyMedium,
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            val modifier = Modifier.size(SUMMONER_SPELL_SIZE.dp)

            summonerSpells.forEach { spell ->
                ReloadableImage(
                    modifier = modifier,
                    url = spell.iconUrl,
                    contentDescription = spell.name,
                    loading = { modifier, _ ->
                        SkeletonRectangle(
                            modifier = modifier,
                        )
                    },
                    error = { modifier, _, onClick ->
                        Failure(
                            modifier = modifier,
                            iconPainter = painterResource(ResLibR.drawable.ic_failure),
                            description = spell.name,
                            onClick = onClick,
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun Runes(runes: RuneSetInfo) {
    Column {
        KeyRune(
            modifier = Modifier
                .align(Alignment.Start)
                .size(PRIMARY_RUNE_SIZE.dp),
            rune = runes.primaryRune
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(RUNE_SPACING.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(RUNE_SPACING.dp)
            ) {
                runes.primaryPath.forEach { rune ->
                    SubsidiaryRune(
                        modifier = Modifier.size(SECONDARY_RUNE_SIZE.dp),
                        rune = rune,
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(RUNE_SPACING.dp)
            ) {
                runes.secondaryPath.forEach { rune ->
                    SubsidiaryRune(
                        modifier = Modifier.size(SECONDARY_RUNE_SIZE.dp),
                        rune = rune,
                    )
                }
            }
        }
    }
}

@Composable
private fun KeyRune(modifier: Modifier, rune: RuneInfo) {
    ReloadableImage(
        modifier = modifier,
        contentDescription = rune.name,
        url = rune.icon,
        loading = { modifier, _ ->
            SkeletonRectangle(modifier = modifier)
        },
        error = { modifier, _, onClick ->
            Failure(
                modifier = modifier,
                iconPainter = painterResource(ResLibR.drawable.ic_failure),
                description = rune.name,
                onClick = onClick,
            )
        }
    )
}

@Composable
private fun SubsidiaryRune(modifier: Modifier, rune: RuneInfo) {
    ReloadableImage(
        modifier = modifier,
        contentDescription = rune.name,
        url = rune.icon,
        loading = { modifier, _ ->
            SkeletonRectangle(modifier = modifier)
        },
        error = { modifier, _, onClick ->
            Failure(
                modifier = modifier,
                iconPainter = painterResource(ResLibR.drawable.ic_failure),
                description = rune.name,
                onClick = onClick,
            )
        }
    )
}

@Composable
private fun KDAAndStats(playerInfo: PlayerInfo) {
    Text(
        text = "${playerInfo.kills} / ${playerInfo.deaths} / ${playerInfo.assists}",
        style = reportMidTypography.bodyMedium,
    )
}


@Preview
@Composable
private fun PreviewMatchDetails() {
    ReportMidTheme {
        Surface {
            MatchDetails(
                modifier = Modifier.fillMaxSize(),
                state = MatchDetailsState(
                    region = Region.EUROPE_WEST,
                    gameType = "Lorem ipsum",
                    duration = "dolor sit amet",
                    teams = persistentListOf(
                        TeamInfo(
                            gameOutcome = "Win",
                            teamSide = TeamSide.Blue,
                            players = List(5) {
                                PlayerInfo(
                                    puuid = EMPTY_STRING,
                                    nameWithTag = "player1#EUW",
                                    summonerLevel = 1,
                                    championIcon = EMPTY_STRING,
                                    championName = "Champ1",
                                    championLevel = 11,
                                    kills = 2,
                                    deaths = 3,
                                    assists = 4,
                                    runes = RuneSetInfo(
                                        primaryRune = RuneInfo(EMPTY_STRING, EMPTY_STRING),
                                        primaryPath = List(3) { RuneInfo(EMPTY_STRING, EMPTY_STRING) }.toPersistentList(),
                                        secondaryPath = List(2) { RuneInfo(EMPTY_STRING, EMPTY_STRING) }.toPersistentList(),
                                    ),
                                    summonerSpells = List(2) { SummonerSpellInfo(iconUrl = EMPTY_STRING, name = EMPTY_STRING) }.toPersistentList(),
                                    items = List(6) { ItemInfo.Unknown }.toPersistentList(),
                                    ward = ItemInfo.Unknown
                                )
                            }.toPersistentList()
                        ),
                        TeamInfo(
                            gameOutcome = "Lose",
                            teamSide = TeamSide.Red,
                            players = List(5) {
                                PlayerInfo(
                                    puuid = EMPTY_STRING,
                                    nameWithTag = "player1#EUW",
                                    summonerLevel = 1,
                                    championIcon = EMPTY_STRING,
                                    championName = "Champ1",
                                    championLevel = 11,
                                    kills = 2,
                                    deaths = 3,
                                    assists = 4,
                                    runes = RuneSetInfo(
                                        primaryRune = RuneInfo(EMPTY_STRING, EMPTY_STRING),
                                        primaryPath = List(3) { RuneInfo(EMPTY_STRING, EMPTY_STRING) }.toPersistentList(),
                                        secondaryPath = List(2) { RuneInfo(EMPTY_STRING, EMPTY_STRING) }.toPersistentList(),
                                    ),
                                    summonerSpells = List(2) { SummonerSpellInfo(iconUrl = EMPTY_STRING, name = EMPTY_STRING) }.toPersistentList(),
                                    items = List(6) { ItemInfo.Unknown }.toPersistentList(),
                                    ward = ItemInfo.Unknown
                                )
                            }.toPersistentList()
                        ),
                    )
                ),
                onPlayerClick = {},
            )
        }
    }
}