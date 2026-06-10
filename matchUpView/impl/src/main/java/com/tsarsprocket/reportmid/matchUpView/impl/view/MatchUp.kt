package com.tsarsprocket.reportmid.matchUpView.impl.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tsarsprocket.reportmid.lol.api.domain.model.Region
import com.tsarsprocket.reportmid.matchUpView.impl.R
import com.tsarsprocket.reportmid.matchUpView.impl.viewIntent.StartLoadingParticipantAccountIntent
import com.tsarsprocket.reportmid.matchUpView.impl.viewState.AccountInfo
import com.tsarsprocket.reportmid.matchUpView.impl.viewState.MatchUpState
import com.tsarsprocket.reportmid.matchUpView.impl.viewState.ParticipantInfo
import com.tsarsprocket.reportmid.matchUpView.impl.viewState.RuneIconInfo
import com.tsarsprocket.reportmid.matchUpView.impl.viewState.SummonerInfo as SummonerInfoState
import com.tsarsprocket.reportmid.matchUpView.impl.viewState.TeamInfo
import com.tsarsprocket.reportmid.theme.ReportMidSpecialColors
import com.tsarsprocket.reportmid.theme.reportMidColorScheme
import com.tsarsprocket.reportmid.theme.reportMidTypography
import com.tsarsprocket.reportmid.utils.annotations.Temporary
import com.tsarsprocket.reportmid.utils.compose.Failure
import com.tsarsprocket.reportmid.utils.compose.ReloadableImage
import com.tsarsprocket.reportmid.utils.compose.SkeletonRectangle
import com.tsarsprocket.reportmid.viewStateApi.view.Loadable
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.PreviewViewStateHolder
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import com.tsarsprocket.reportmid.viewStateApi.viewState.LoadablePart
import kotlinx.coroutines.launch
import com.tsarsprocket.reportmid.resLib.R as ResLibR

private const val CHAMPION_ICON_SIZE_DP = 48
private const val PRIMARY_RUNE_ICON_SIZE_DP = 20
private const val RUNE_ICON_SIZE_DP = 16
private const val SPELL_ICON_SIZE_DP = 20
private const val SUMMONER_LEVEL_MIN_WIDTH_SP = 24

@Composable
internal fun MatchUp(modifier: Modifier, state: MatchUpState, stateHolder: ViewStateHolder) {
    val teams = state.teams.values.toList()
    val pagerState = rememberPagerState { teams.size }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
        ) { page ->
            val team = teams[page]
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(team.color),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                team.participants.values.forEach { participant ->
                    ParticipantRow(
                        participant = participant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                    ) { puuid ->
                        stateHolder.postIntent(StartLoadingParticipantAccountIntent(team.id, puuid, state.region))
                    }
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
private fun ParticipantRow(modifier: Modifier = Modifier, participant: ParticipantInfo, reloadTrigger: (String) -> Unit) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        GameImage(
            url = participant.championImageUrl,
            modifier = Modifier.size(CHAMPION_ICON_SIZE_DP.dp),
        )

        SummonerInfo(
            account = participant.account,
            summoner = participant.summoner,
            onLoadTrigger = { reloadTrigger(participant.puuid) },
        )

        RuneIcons(
            primaryRune = participant.primaryRune,
            secondaryRuneStyle = participant.secondaryRuneStyle,
        )

        SummonerSpellIcons(
            spell1Url = participant.summonerSpell1ImageUrl,
            spell2Url = participant.summonerSpell2ImageUrl,
        )
    }
}

@Composable
private fun SummonerInfo(account: LoadablePart<AccountInfo>, summoner: LoadablePart<SummonerInfoState>, onLoadTrigger: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        SummonerName(account = account, onLoadTrigger = onLoadTrigger)
        SummonerLevel(summoner = summoner)
    }
}

@Composable
private fun SummonerName(account: LoadablePart<AccountInfo>, onLoadTrigger: () -> Unit) {
    Loadable(
        part = account,
        loading = { modifier ->
            with(LocalDensity.current) {
                SkeletonRectangle(
                    modifier.size(width = 120.sp.toDp(), height = 16.sp.toDp())
                )
            }
        },
        success = { modifier, accountInfo ->
            Text(
                modifier = modifier,
                text = accountInfo.name,
                style = reportMidTypography.bodySmall,
            )
        },
        failure = { modifier, reloader ->
            Text(
                modifier = modifier.clickable(onClick = reloader),
                text = stringResource(R.string.match_up_view_not_loaded),
                style = reportMidTypography.bodySmall,
                color = reportMidColorScheme.error,
            )
        },
        loadTrigger = onLoadTrigger,
    )
}

@Composable
private fun SummonerLevel(summoner: LoadablePart<SummonerInfoState>) {
    Row {
        Text(
            text = stringResource(R.string.match_up_view_summoner_level_label),
            style = reportMidTypography.bodySmall,
        )
        Loadable(
            part = summoner,
            loading = { modifier ->
                with(LocalDensity.current) {
                    SkeletonRectangle(
                        modifier.size(
                            width = SUMMONER_LEVEL_MIN_WIDTH_SP.sp.toDp(),
                            height = reportMidTypography.bodySmall.fontSize.toDp(),
                        )
                    )
                }
            },
            success = { modifier, summonerInfo ->
                Text(
                    modifier = modifier,
                    text = summonerInfo.level,
                    style = reportMidTypography.bodySmall,
                )
            },
            failure = { modifier, _ ->
                Text(
                    modifier = modifier,
                    text = stringResource(R.string.match_up_view_not_loaded),
                    style = reportMidTypography.bodySmall,
                    color = reportMidColorScheme.error,
                )
            },
            loadTrigger = {},
        )
    }
}

@Composable
private fun RuneIcons(primaryRune: RuneIconInfo, secondaryRuneStyle: RuneIconInfo) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        GameImage(
            url = primaryRune.imageUrl,
            contentDescription = primaryRune.title,
            modifier = Modifier.size(PRIMARY_RUNE_ICON_SIZE_DP.dp),
        )
        GameImage(
            url = secondaryRuneStyle.imageUrl,
            contentDescription = secondaryRuneStyle.title,
            modifier = Modifier.size(RUNE_ICON_SIZE_DP.dp),
        )
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
private fun GameImage(modifier: Modifier = Modifier, url: String, contentDescription: String? = null) {
    ReloadableImage(
        url = url,
        modifier = modifier,
        contentDescription = contentDescription,
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
            teams = mapOf(
                100 to TeamInfo(
                    id = 100,
                    isBlueSide = true,
                    participants = mapOf(
                        "puuid-1" to previewParticipant("puuid-1", "Faker#T1"),
                        "puuid-2" to previewParticipant("puuid-2", "Caps#EUW"),
                        "puuid-3" to previewParticipant("puuid-3", "Rekkles#NA1"),
                        "puuid-4" to previewParticipant("puuid-4", "Jankos#EUW"),
                        "puuid-5" to previewParticipant("puuid-5", "Mikyx#EUW"),
                    ),
                ),
                200 to TeamInfo(
                    id = 200,
                    isBlueSide = false,
                    participants = mapOf(
                        "puuid-6" to previewParticipant("puuid-6", "Uzi#KR1"),
                        "puuid-7" to previewParticipant("puuid-7", "Rookie#LPL"),
                        "puuid-8" to previewParticipant("puuid-8", "TheShy#LPL"),
                        "puuid-9" to previewParticipant("puuid-9", "Karsa#LPL"),
                        "puuid-10" to previewParticipant("puuid-10", "Ming#LPL"),
                    ),
                ),
            ),
        ),
        stateHolder = PreviewViewStateHolder,
    )
}

private fun previewParticipant(puuid: String, name: String) = ParticipantInfo(
    puuid = puuid,
    championImageUrl = "",
    account = LoadablePart.Loading,
    summoner = LoadablePart.Loading,
    primaryRune = RuneIconInfo("", ""),
    secondaryRuneStyle = RuneIconInfo("", ""),
    summonerSpell1ImageUrl = "",
    summonerSpell2ImageUrl = "",
)
