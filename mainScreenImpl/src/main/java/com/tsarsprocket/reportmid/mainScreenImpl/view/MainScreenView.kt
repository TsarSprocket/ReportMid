package com.tsarsprocket.reportmid.mainScreenImpl.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tsarsprocket.reportmid.mainScreenImpl.viewState.MainScreenViewState

@Composable
fun MainScreenView(modifier: Modifier, state: MainScreenViewState) {
    Scaffold(modifier = modifier) { paddings ->
        Column(
            modifier = Modifier.padding(paddings),
        ) {
            state.appBarStateHolder.Visualize(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxWidth(),
            )

            state.summonerStateHolder.Visualize(
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}