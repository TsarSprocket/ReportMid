package com.tsarsprocket.reportmid.matchUpView.impl.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.tsarsprocket.reportmid.matchUpView.impl.viewIntent.LoadMatchUpIntent
import com.tsarsprocket.reportmid.matchUpView.impl.viewState.LoadingState
import com.tsarsprocket.reportmid.utils.compose.screens.DefaultLoadingScreen
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder

@Composable
internal fun Loading(modifier: Modifier, state: LoadingState, stateHolder: ViewStateHolder) {
    LaunchedEffect(Unit) {
        stateHolder.postIntent(LoadMatchUpIntent(state.puuid, state.region))
    }

    DefaultLoadingScreen(modifier = modifier)
}
