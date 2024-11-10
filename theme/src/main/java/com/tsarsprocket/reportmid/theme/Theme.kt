package com.tsarsprocket.reportmid.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle

@Composable
fun ReportMidTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = reportMidColorScheme,
    ) {
        ProvideTextStyle(
            value = TextStyle(color = reportMidColorScheme.primary),
            content = content,
        )
    }
}
