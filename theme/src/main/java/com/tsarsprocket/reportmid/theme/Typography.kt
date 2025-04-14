package com.tsarsprocket.reportmid.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable

val reportMidTypography: Typography
    @Composable get() = Typography(
        bodySmall = MaterialTheme.typography.bodySmall.copy(fontFamily = reportMidFontFamily),
        bodyMedium = MaterialTheme.typography.bodyMedium.copy(fontFamily = reportMidFontFamily),
        bodyLarge = MaterialTheme.typography.bodyLarge.copy(fontFamily = reportMidFontFamily),
        displaySmall = MaterialTheme.typography.displaySmall.copy(fontFamily = reportMidFontFamily),
        displayMedium = MaterialTheme.typography.displayMedium.copy(fontFamily = reportMidFontFamily),
        displayLarge = MaterialTheme.typography.displayLarge.copy(fontFamily = reportMidFontFamily),
        headlineSmall = MaterialTheme.typography.headlineSmall.copy(fontFamily = reportMidFontFamily),
        headlineMedium = MaterialTheme.typography.headlineMedium.copy(fontFamily = reportMidFontFamily),
        headlineLarge = MaterialTheme.typography.headlineLarge.copy(fontFamily = reportMidFontFamily),
        labelSmall = MaterialTheme.typography.labelSmall.copy(fontFamily = reportMidFontFamily),
        labelMedium = MaterialTheme.typography.labelMedium.copy(fontFamily = reportMidFontFamily),
        labelLarge = MaterialTheme.typography.labelLarge.copy(fontFamily = reportMidFontFamily),
        titleSmall = MaterialTheme.typography.titleSmall.copy(fontFamily = reportMidFontFamily),
        titleMedium = MaterialTheme.typography.titleMedium.copy(fontFamily = reportMidFontFamily),
        titleLarge = MaterialTheme.typography.titleLarge.copy(fontFamily = reportMidFontFamily),
    )