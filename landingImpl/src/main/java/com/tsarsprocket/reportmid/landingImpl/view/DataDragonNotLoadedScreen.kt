package com.tsarsprocket.reportmid.landingImpl.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tsarsprocket.reportmid.landingImpl.R
import com.tsarsprocket.reportmid.theme.ReportMidTheme
import com.tsarsprocket.reportmid.theme.reportMidTypography

@Composable
internal fun DataDragonNotLoadedScreen(isLoading: Boolean, onRetry: () -> Unit) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                space = 40.dp,
                alignment = Alignment.CenterVertically,
            ),
        ) {
            Text(
                text = stringResource(R.string.data_dragon_not_loaded_title),
                style = reportMidTypography.titleLarge,
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth(0.8f),
                text = stringResource(R.string.data_dragon_not_loaded_message),
                style = reportMidTypography.bodyLarge,
                textAlign = TextAlign.Center,
            )

            Button(
                modifier = Modifier.width(120.dp),
                onClick = onRetry,
                enabled = !isLoading,
            ) {
                if(isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                    )
                } else {
                    Text(
                        text = stringResource(R.string.data_dragon_not_loaded_retry),
                    )
                }
            }
        }
    }
}


@Preview
@Composable
private fun DataDragonNotLoadedScreenPreviewNoLoading() {
    ReportMidTheme {
        DataDragonNotLoadedScreen(isLoading = false) {}
    }
}

@Preview
@Composable
private fun DataDragonNotLoadedScreenPreviewLoading() {
    ReportMidTheme {
        DataDragonNotLoadedScreen(isLoading = true) {}
    }
}
