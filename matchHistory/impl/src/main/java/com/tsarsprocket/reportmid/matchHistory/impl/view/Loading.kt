package com.tsarsprocket.reportmid.matchHistory.impl.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tsarsprocket.reportmid.utils.compose.screens.DefaultLoadingScreen

@Composable
internal fun Loading(modifier: Modifier) {
    DefaultLoadingScreen(modifier = modifier.fillMaxSize())
}