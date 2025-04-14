package com.tsarsprocket.reportmid.profileOverviewImpl.view

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.tsarsprocket.reportmid.profileOverviewImpl.R
import com.tsarsprocket.reportmid.profileOverviewImpl.viewState.ProfileOverviewStateCluster.ProfileState
import com.tsarsprocket.reportmid.theme.ReportMidTheme
import com.tsarsprocket.reportmid.theme.reportMidTypography
import com.tsarsprocket.reportmid.utils.common.HASH
import com.tsarsprocket.reportmid.utils.compose.Failure
import com.tsarsprocket.reportmid.utils.compose.SkeletonRectangle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf


private const val RETRY_ATTEMPT = "retry_attempt"
private const val SUMMONER_ICON_SIZE = 48

@Composable
internal fun ProfileOverview(
    modifier: Modifier,
    state: ProfileState,
) {
    Column(
        modifier = modifier
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
}

@Composable
private fun IconFailure(size: Dp, onClick: () -> Unit) {
    Failure(
        modifier = Modifier
            .size(size),
        iconPainter = painterResource(com.tsarsprocket.reportmid.resLib.R.drawable.ic_failure),
        iconSize = size * 0.66f,
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
        masteries.forEach { mastery ->
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = CenterHorizontally,
            ) {
                var retryCount by remember { mutableIntStateOf(0) }

                SubcomposeAsyncImage(
                    modifier = Modifier.size(iconSize),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(mastery.championImageUrl)
                        .setParameter(RETRY_ATTEMPT, retryCount)
                        .build(),
                    contentDescription = mastery.championName,
                    loading = {
                        SkeletonRectangle(modifier = Modifier.size(iconSize))
                    },
                    error = {
                        IconFailure(iconSize) {
                            ++retryCount
                        }
                    }
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
    var retryCount by remember { mutableIntStateOf(0) }

    SubcomposeAsyncImage(
        modifier = Modifier.size(size),
        model = ImageRequest.Builder(LocalContext.current)
            .data(icon)
            .setParameter(RETRY_ATTEMPT, retryCount)
            .build(),
        contentDescription = stringResource(R.string.profile_overview_summoner_icon),
        loading = {
            SkeletonRectangle(Modifier.size(size))
        },
        error = {
            IconFailure(size) {
                ++retryCount
            }
        }
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
                )
            )
        }
    }
}
