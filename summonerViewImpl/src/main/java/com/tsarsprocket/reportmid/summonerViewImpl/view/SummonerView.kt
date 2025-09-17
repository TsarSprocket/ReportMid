package com.tsarsprocket.reportmid.summonerViewImpl.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.tsarsprocket.reportmid.summonerViewImpl.viewState.SummonerViewState
import com.tsarsprocket.reportmid.summonerViewImpl.viewState.SummonerViewState.ActivePage.MATCH_HISTORY
import com.tsarsprocket.reportmid.summonerViewImpl.viewState.SummonerViewState.ActivePage.PROFILE
import com.tsarsprocket.reportmid.theme.reportMidColorScheme
import com.tsarsprocket.reportmid.theme.reportMidTypography
import com.tsarsprocket.reportmid.resLib.R as ResLib

@Composable
internal fun SummonerView(
    modifier: Modifier,
    selected: SummonerViewState.ActivePage,
    selectProfile: () -> Unit,
    selectMatchHistory: () -> Unit,
    content: @Composable (Modifier) -> Unit,
) {
    Column(modifier = modifier) {
        content(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        NavigationBar(
            modifier = Modifier.fillMaxWidth(),
        ) {
            NavigationBarItem(
                selected = selected == PROFILE,
                onClick = selectProfile,
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = null,
                    )
                },
                label = {
                    Text(
                        text = "Profile",
                        color = reportMidColorScheme.primary,
                        style = reportMidTypography.bodyMedium,
                    )
                }
            )

            NavigationBarItem(
                selected = selected == MATCH_HISTORY,
                onClick = selectMatchHistory,
                icon = {
                    Icon(
                        painter = painterResource(ResLib.drawable.ic_game_history),
                        contentDescription = null,
                    )
                },
                label = {
                    Text(
                        text = "History",
                        color = reportMidColorScheme.primary,
                        style = reportMidTypography.bodyMedium,
                    )
                }
            )
        }
    }
}