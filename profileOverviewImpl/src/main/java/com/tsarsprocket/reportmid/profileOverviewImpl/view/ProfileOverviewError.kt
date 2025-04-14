package com.tsarsprocket.reportmid.profileOverviewImpl.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.tsarsprocket.reportmid.profileOverviewImpl.R
import com.tsarsprocket.reportmid.theme.ReportMidTheme
import com.tsarsprocket.reportmid.utils.compose.screens.ErrorScreen

@Composable
internal fun ProfileOverviewError(
    modifier: Modifier,
    onRetry: () -> Unit,
) {
    ErrorScreen(
        modifier = modifier,
        title = stringResource(R.string.profile_overview_error_title),
        message = stringResource(R.string.profile_overview_error_text),
        buttonTitle = stringResource(R.string.profile_overview_error_retry),
        onButtonAction = onRetry,
    )
}


@Preview
@Composable
private fun ProfileOverviewErrorPreview() {
    ReportMidTheme {
        Scaffold { paddingValues ->
            ProfileOverviewError(
                Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {}
        }
    }
}
