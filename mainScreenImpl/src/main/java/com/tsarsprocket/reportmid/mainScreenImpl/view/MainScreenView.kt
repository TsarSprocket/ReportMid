package com.tsarsprocket.reportmid.mainScreenImpl.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MainScreenView(modifier: Modifier, content: @Composable (Modifier) -> Unit) {
    Scaffold(modifier = modifier) { paddings ->
        Box(modifier = Modifier.padding(paddings)) {
            content(Modifier.fillMaxSize())
        }
    }
}