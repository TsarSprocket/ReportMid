package com.tsarsprocket.reportmid.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun ReportMidTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = reportMidColorScheme,
        content = content
    )
}

@Composable
fun PreviewTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LocalPreviewColorScheme.current,
        content = content
    )
}