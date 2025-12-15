package com.tsarsprocket.reportmid.utils.compose.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DefaultLoadingScreen(modifier: Modifier) {
    Box(
        modifier,
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .padding(4.dp)
                .size(48.dp)
                .align(Alignment.Center),
        )

    }
}