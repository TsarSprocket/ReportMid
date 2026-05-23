package com.tsarsprocket.reportmid.matchUpView.impl.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.tsarsprocket.reportmid.matchUpView.impl.viewIntent.LoadMatchUpViewIntent
import com.tsarsprocket.reportmid.matchUpView.impl.viewState.LoadingViewState
import com.tsarsprocket.reportmid.utils.compose.screens.DefaultLoadingScreen
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder

@Composable
internal fun Loading(modifier: Modifier, state: LoadingViewState, stateHolder: ViewStateHolder) {
    LaunchedEffect(Unit) {
        stateHolder.postIntent(LoadMatchUpViewIntent(state.puuid, state.region))
    }

    DefaultLoadingScreen(modifier = modifier)
}
