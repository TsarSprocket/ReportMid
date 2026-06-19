package com.tsarsprocket.reportmid.utils.compose.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.tsarsprocket.reportmid.theme.reportMidColorScheme
import com.tsarsprocket.reportmid.utils.compose.RefreshButton
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Composable
fun DefaultRefreshPanel(
    modifier: Modifier = Modifier,
    initiallyDisabledDuration: Duration,
    initiallyDisabledPercent: Float = 1f,
    onRefreshPressed: () -> Unit,
) {
    val currentOnRefreshPressed by rememberUpdatedState(onRefreshPressed)
    var pressCount by remember { mutableIntStateOf(0) }

    val untilEnabled = remember {
        Animatable(if (initiallyDisabledDuration == Duration.ZERO) 0f else initiallyDisabledPercent)
    }
    var enabled by remember {
        mutableStateOf(initiallyDisabledDuration == Duration.ZERO)
    }

    LaunchedEffect(pressCount, initiallyDisabledDuration) {
        if (initiallyDisabledDuration == Duration.ZERO) {
            untilEnabled.snapTo(0f)
            enabled = true
        } else {
            enabled = false
            untilEnabled.snapTo(initiallyDisabledPercent)
            untilEnabled.animateTo(
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = initiallyDisabledDuration.inWholeMilliseconds.toInt(),
                    easing = LinearEasing,
                ),
            )
            enabled = true
        }
    }

    Box(modifier = modifier) {
        RefreshButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 64.dp, end = 40.dp),
            enabled = enabled,
            untilEnabled = untilEnabled.value,
            onClick = {
                currentOnRefreshPressed()
                pressCount++
            },
        )
    }
}


@Preview
@Composable
private fun DefaultRefreshPanelZeroDurationPreview() {
    MaterialTheme(colorScheme = reportMidColorScheme) {
        DefaultRefreshPanel(
            modifier = Modifier.size(300.dp, 300.dp),
            initiallyDisabledDuration = Duration.ZERO,
            onRefreshPressed = {},
        )
    }
}


@Preview
@Composable
private fun DefaultRefreshPanelTenSecPreview() {
    MaterialTheme(colorScheme = reportMidColorScheme) {
        DefaultRefreshPanel(
            modifier = Modifier.size(300.dp, 300.dp),
            initiallyDisabledDuration = 10.seconds,
            onRefreshPressed = {},
        )
    }
}
