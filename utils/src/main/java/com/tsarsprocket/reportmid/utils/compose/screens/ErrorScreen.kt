package com.tsarsprocket.reportmid.utils.compose.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.tsarsprocket.reportmid.theme.reportMidTypography

@Composable
fun ErrorScreen(modifier: Modifier, title: String, message: String, buttonTitle: String, onButtonAction: () -> Unit) {
    var areaSize by remember { mutableStateOf(IntSize.Zero) }

    Box(
        modifier
            .fillMaxSize()
            .onSizeChanged { areaSize = it }
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = with(LocalDensity.current) { (areaSize.width * 0.12f).toDp() }),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        top = with(LocalDensity.current) { (areaSize.width * 0.5f).toDp() },
                        bottom = with(LocalDensity.current) { (areaSize.width * 0.6f).toDp() },
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                // TODO Place some picture here

                Text(
                    text = title,
                    style = reportMidTypography.titleLarge,
                    textAlign = TextAlign.Center,
                )

                Text(
                    modifier = Modifier.padding(top = 80.dp),
                    text = message,
                    style = reportMidTypography.bodyLarge,
                    textAlign = TextAlign.Center,
                )
            }

            Button(
                modifier = Modifier.padding(bottom = 48.dp),
                onClick = onButtonAction,
            ) {
                Text(
                    text = buttonTitle,
                    style = reportMidTypography.bodyLarge,
                )
            }
        }
    }
}
