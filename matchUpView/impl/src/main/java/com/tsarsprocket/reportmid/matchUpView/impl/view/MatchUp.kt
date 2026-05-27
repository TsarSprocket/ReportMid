package com.tsarsprocket.reportmid.matchUpView.impl.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tsarsprocket.reportmid.lol.api.domain.model.Region
import com.tsarsprocket.reportmid.matchUpView.impl.viewState.MatchUpState
import com.tsarsprocket.reportmid.matchUpView.impl.viewState.ParticipantInfo
import com.tsarsprocket.reportmid.matchUpView.impl.viewState.TeamInfo
import com.tsarsprocket.reportmid.theme.ReportMidSpecialColors
import com.tsarsprocket.reportmid.theme.reportMidTypography
import com.tsarsprocket.reportmid.utils.annotations.Temporary
import com.tsarsprocket.reportmid.utils.compose.Failure
import com.tsarsprocket.reportmid.utils.compose.ReloadableImage
import com.tsarsprocket.reportmid.utils.compose.SkeletonRectangle
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.PreviewViewStateHolder
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import kotlinx.coroutines.launch
import com.tsarsprocket.reportmid.resLib.R as ResLibR

private const val CHAMPION_ICON_SIZE_DP = 48
private const val RUNE_ICON_SIZE_DP = 16
private const val SPELL_ICON_SIZE_DP = 20

@Composable
internal fun MatchUp(modifier: Modifier, state: MatchUpState, stateHolder: ViewStateHolder) {
    val teams = state.teams
    val pagerState = rememberPagerState { teams.size }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
        ) { page ->
            val team = teams[page]
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(team.color),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                items(team.participants) { participant ->
                    ParticipantRow(
                        participant = participant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                    )
                }
            }
        }

        SecondaryTabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = Color.Transparent,
            modifier = Modifier.fillMaxWidth(),
        ) {
            teams.forEachIndexed { index, team ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                    modifier = Modifier.background(team.color),
                    text = { Text(@Temporary("Change to icons") if(team.isBlueSide) "blue" else "red") },
                )
            }
        }
    }
}

@Composable
private fun ParticipantRow(modifier: Modifier = Modifier, participant: ParticipantInfo) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        GameImage(
            url = participant.championImageUrl,
            modifier = Modifier.size(CHAMPION_ICON_SIZE_DP.dp),
        )

        Text(
            text = participant.summonerDisplayName,
            style = reportMidTypography.bodyMedium,
        )

        RuneIcons(
            primaryUrls = participant.primaryRuneImageUrls,
            secondaryUrls = participant.secondaryRuneImageUrls,
        )

        SummonerSpellIcons(
            spell1Url = participant.summonerSpell1ImageUrl,
            spell2Url = participant.summonerSpell2ImageUrl,
        )
    }
}

@Composable
private fun RuneIcons(primaryUrls: List<String>, secondaryUrls: List<String>) {
    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            primaryUrls.forEach { url ->
                GameImage(
                    url = url,
                    modifier = Modifier.size(RUNE_ICON_SIZE_DP.dp),
                )
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            secondaryUrls.forEach { url ->
                GameImage(
                    url = url,
                    modifier = Modifier.size(RUNE_ICON_SIZE_DP.dp),
                )
            }
        }
    }
}

@Composable
private fun SummonerSpellIcons(spell1Url: String, spell2Url: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        GameImage(
            url = spell1Url,
            modifier = Modifier.size(SPELL_ICON_SIZE_DP.dp),
        )
        GameImage(
            url = spell2Url,
            modifier = Modifier.size(SPELL_ICON_SIZE_DP.dp),
        )
    }
}

@Composable
private fun GameImage(modifier: Modifier = Modifier, url: String) {
    ReloadableImage(
        url = url,
        modifier = modifier,
        contentDescription = null,
        loading = { mod, _ ->
            SkeletonRectangle(modifier = mod, cornerSize = 1.dp)
        },
        error = { mod, _, retry ->
            Failure(
                modifier = mod,
                iconPainter = painterResource(ResLibR.drawable.ic_failure),
                onClick = retry,
            )
        },
    )
}

private val TeamInfo.color: Color
    get() = if(isBlueSide) ReportMidSpecialColors.blueTeam else ReportMidSpecialColors.redTeam


@Preview
@Composable
private fun MatchUpPreview() {
    MatchUp(
        modifier = Modifier,
        state = MatchUpState(
            puuid = "preview-puuid",
            region = Region.EUROPE_WEST,
            teams = listOf(
                TeamInfo(
                    isBlueSide = true,
                    participants = listOf(
                        previewParticipant("Faker#T1"),
                        previewParticipant("Caps#EUW"),
                        previewParticipant("Rekkles#NA1"),
                        previewParticipant("Jankos#EUW"),
                        previewParticipant("Mikyx#EUW"),
                    ),
                ),
                TeamInfo(
                    isBlueSide = false,
                    participants = listOf(
                        previewParticipant("Uzi#KR1"),
                        previewParticipant("Rookie#LPL"),
                        previewParticipant("TheShy#LPL"),
                        previewParticipant("Karsa#LPL"),
                        previewParticipant("Ming#LPL"),
                    ),
                ),
            ),
        ),
        stateHolder = PreviewViewStateHolder,
    )
}

private fun previewParticipant(name: String) = ParticipantInfo(
    championImageUrl = "",
    summonerDisplayName = name,
    primaryRuneImageUrls = listOf("", "", "", ""),
    secondaryRuneImageUrls = listOf("", ""),
    summonerSpell1ImageUrl = "",
    summonerSpell2ImageUrl = "",
)
