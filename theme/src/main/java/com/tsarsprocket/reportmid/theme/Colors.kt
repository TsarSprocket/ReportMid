package com.tsarsprocket.reportmid.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val reportMidColorScheme: ColorScheme
    @Composable
    get() = lightColorScheme(
        background = Color(0xFF303030),
        onBackground = Color(0xFFE6EE9C),
        onPrimary = Color(0xE0000000),
        onSurface = Color(0xFFE6EE9C),
        onSurfaceVariant = Color(0xFF576C88),
        primary = Color(0xFFE6EE9C),
        secondary = Color(0xFF59801A),
        surface = Color(0xFF303030),
        surfaceVariant = Color(0xFF272730),
        onTertiaryContainer = Color(0xFF214752),
        tertiaryContainer = Color(0xFF00212B),
        surfaceContainerLow = Color(0xFF181818),
        surfaceContainer = Color(0xFF1A1A1A),
    )
