package com.tsarsprocket.reportmid.matchUpView.impl.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.tsarsprocket.reportmid.lol.api.domain.model.Region
import com.tsarsprocket.reportmid.matchUpView.impl.viewState.MatchUpState
import com.tsarsprocket.reportmid.matchUpView.impl.viewState.TeamInfo
import com.tsarsprocket.reportmid.theme.ReportMidSpecialColors
import com.tsarsprocket.reportmid.utils.annotations.Temporary
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.PreviewViewStateHolder
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import kotlinx.coroutines.launch

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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(teams[page].color),
            ) {
                /* TODO Team members go here */
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
                TeamInfo(isBlueSide = true),
                TeamInfo(isBlueSide = false),
            ),
        ),
        stateHolder = PreviewViewStateHolder,
    )
}
