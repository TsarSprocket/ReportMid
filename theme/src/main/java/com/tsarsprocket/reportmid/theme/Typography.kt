package com.tsarsprocket.reportmid.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable

val reportMidTypography: Typography
    @Composable get() = Typography(
        bodyLarge = MaterialTheme.typography.bodyLarge.copy(fontFamily = reportMidFontFamily),
        displayLarge = MaterialTheme.typography.displayMedium.copy(fontFamily = reportMidFontFamily),
        labelSmall = MaterialTheme.typography.labelSmall.copy(fontFamily = reportMidFontFamily),
        labelMedium = MaterialTheme.typography.labelMedium.copy(fontFamily = reportMidFontFamily),
        labelLarge = MaterialTheme.typography.labelLarge.copy(fontFamily = reportMidFontFamily),
    )