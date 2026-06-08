package com.tsarsprocket.reportmid.matchUpView.impl.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tsarsprocket.reportmid.lol.api.domain.model.Region
import com.tsarsprocket.reportmid.matchUpView.impl.viewIntent.LoadMatchUpIntent
import com.tsarsprocket.reportmid.matchUpView.impl.viewState.ErrorState
import com.tsarsprocket.reportmid.theme.ReportMidTheme
import com.tsarsprocket.reportmid.theme.reportMidColorScheme
import com.tsarsprocket.reportmid.theme.reportMidTypography
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.PreviewViewStateHolder
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder

@Composable
internal fun Error(modifier: Modifier, state: ErrorState, stateHolder: ViewStateHolder) {
    // TODO: Replace with actual
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 40.dp),
        verticalArrangement = Arrangement.spacedBy(48.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Cannot load match up",
            style = reportMidTypography.headlineMedium,
            color = reportMidColorScheme.error,
            textAlign = TextAlign.Center
        )

        Button(
            onClick = { stateHolder.postIntent(LoadMatchUpIntent(state.puuid, state.region)) },
        ) {
            Text(
                text = "Reload",
                style = reportMidTypography.bodyLarge,
            )
        }
    }
}


@Preview
@Composable
private fun ErrorPreview() {
    ReportMidTheme {
        Surface {
            Error(
                modifier = Modifier,
                state = ErrorState("", Region.EUROPE_WEST, ""),
                stateHolder = PreviewViewStateHolder,
            )
        }
    }
}
