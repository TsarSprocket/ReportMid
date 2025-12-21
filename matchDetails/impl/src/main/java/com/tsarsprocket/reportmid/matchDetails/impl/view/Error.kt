package com.tsarsprocket.reportmid.matchDetails.impl.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.tsarsprocket.reportmid.matchDetails.impl.R
import com.tsarsprocket.reportmid.utils.compose.screens.ErrorScreen

@Composable
internal fun ErrorNotLoaded(modifier: Modifier, onReload: () -> Unit) {
    ErrorScreen(
        modifier = modifier,
        title = stringResource(R.string.match_details_error_not_loaded_title),
        message = stringResource(R.string.match_details_error_not_loaded_message),
        buttonTitle = stringResource(R.string.match_details_error_not_loaded_button),
        onButtonAction = onReload,
    )
}