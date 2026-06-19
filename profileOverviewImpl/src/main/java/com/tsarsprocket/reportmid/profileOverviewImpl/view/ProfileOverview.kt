package com.tsarsprocket.reportmid.profileOverviewImpl.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tsarsprocket.reportmid.lol.api.domain.model.Region
import com.tsarsprocket.reportmid.profileOverviewApi.viewIntent.ProfileOverviewViewIntent
import com.tsarsprocket.reportmid.profileOverviewImpl.R
import com.tsarsprocket.reportmid.profileOverviewImpl.viewState.ProfileOverviewStateCluster.ProfileState
import com.tsarsprocket.reportmid.theme.ReportMidTheme
import com.tsarsprocket.reportmid.theme.reportMidTypography
import com.tsarsprocket.reportmid.utils.common.HASH
import com.tsarsprocket.reportmid.utils.common.REFRESH_DISABLED_DURATION_MS
import com.tsarsprocket.reportmid.utils.compose.Failure
import com.tsarsprocket.reportmid.utils.compose.ReloadableImage
import com.tsarsprocket.reportmid.utils.compose.SkeletonRectangle
import com.tsarsprocket.reportmid.utils.compose.screens.DefaultRefreshPanel
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.PreviewViewStateHolder
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import com.tsarsprocket.reportmid.resLib.R as ResLib


@Composable
internal fun ProfileOverview(
    modifier: Modifier,
    state: ProfileState,
    stateHolder: ViewStateHolder,
) {
    val (refreshDisabledDuration, refreshDisabledPercent) = remember(state.lastLoadedAt) {
        val remainingMillis = (state.lastLoadedAt + REFRESH_DISABLED_DURATION_MS) - System.currentTimeMillis()
        if(remainingMillis > 0) {
            remainingMillis.milliseconds to (remainingMillis.toFloat() / REFRESH_DISABLED_DURATION_MS).coerceAtMost(1f)
        } else {
            Duration.ZERO to 0f
        }
    }

    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 36.dp),
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Spacer(modifier = Modifier.weight(1f))

            SummonerIcon(
                size = 128.dp,
                icon = state.icon,
            )

            Name(
                modifier = Modifier
                    .padding(top = 48.dp),
                gameName = state.name,
                tagLine = state.tagLine,
                textStyle = reportMidTypography.bodyLarge,
            )

            Level(
                modifier = Modifier.padding(top = 16.dp),
                level = state.level,
                textStyle = reportMidTypography.bodyLarge,
            )

            Masteries(
                modifier = Modifier.padding(top = 48.dp),
                masteries = state.masteries,
                iconSize = 48.dp,
            )

            Spacer(modifier = Modifier.weight(1.2f))
        }

        DefaultRefreshPanel(
            modifier = Modifier.fillMaxSize(),
            initiallyDisabledDuration = refreshDisabledDuration,
            initiallyDisabledPercent = refreshDisabledPercent,
            onRefreshPressed = { stateHolder.postIntent(ProfileOverviewViewIntent(state.puuid, state.region)) },
        )
    }
}

@Composable
private fun IconFailure(modifier: Modifier, onClick: () -> Unit) {
    Failure(
        modifier = modifier,
        iconPainter = painterResource(ResLib.drawable.ic_failure),
        description = stringResource(R.string.profile_overview_summoner_icon_not_loaded),
        onClick = onClick,
    )
}

@Composable
private fun Level(
    modifier: Modifier,
    level: Int,
    textStyle: TextStyle,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = stringResource(R.string.profile_overview_summoner_level_label),
            style = textStyle,
        )

        Text(
            text = level.toString(),
            style = textStyle,
            textAlign = TextAlign.Start
        )
    }
}

@Composable
private fun Masteries(modifier: Modifier, masteries: ImmutableList<ProfileState.Mastery>, iconSize: Dp) {
    Row(
        modifier = modifier.fillMaxWidth(),
    ) {
        val iconModifier = Modifier.size(iconSize)

        masteries.forEach { mastery ->
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = CenterHorizontally,
            ) {
                ReloadableImage(
                    modifier = iconModifier,
                    url = mastery.championImageUrl,
                    contentDescription = mastery.championName,
                    loading = { modifier, _ ->
                        SkeletonRectangle(modifier = modifier)
                    },
                    error = { modifier, _, onClick ->
                        IconFailure(modifier, onClick = onClick)
                    },
                )

                Text(
                    modifier = Modifier.padding(top = 16.dp),
                    text = mastery.level,
                    style = reportMidTypography.bodyMedium,
                )

                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = mastery.points,
                    style = reportMidTypography.bodySmall,
                )
            }
        }
    }
}

@Composable
private fun Name(
    modifier: Modifier,
    gameName: String,
    tagLine: String,
    textStyle: TextStyle,
) {
    Row(
        modifier = modifier,
    ) {
        Text(
            text = gameName,
            style = textStyle,
        )

        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = HASH,
            style = textStyle,
        )

        Text(
            text = tagLine,
            style = textStyle,
        )
    }
}

@Composable
private fun SummonerIcon(
    size: Dp,
    icon: String,
) {
    val modifier = Modifier.size(size)

    ReloadableImage(
        modifier = modifier,
        url = icon,
        contentDescription = stringResource(R.string.profile_overview_summoner_icon),
        loading = { modifier, _ ->
            SkeletonRectangle(modifier)
        },
        error = { modifier, _, onClick ->
            IconFailure(modifier = modifier, onClick = onClick)
        },
    )
}

@Preview
@Composable
private fun ProfileOverviewPreview() {
    ReportMidTheme {
        Scaffold { paddingValues ->
            ProfileOverview(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                state = ProfileState(
                    puuid = "preview-puuid",
                    region = Region.EUROPE_WEST,
                    icon = "",
                    name = "Lorem ipsum",
                    tagLine = "dolor",
                    level = 123,
                    masteries = persistentListOf(
                        ProfileState.Mastery(
                            championImageUrl = "",
                            championName = "Lorem",
                            level = "5",
                            points = "12 345",
                        ),
                    )
                ),
                stateHolder = PreviewViewStateHolder,
            )
        }
    }
}
