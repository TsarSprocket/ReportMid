package com.tsarsprocket.reportmid.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource

val reportMidColorScheme: ColorScheme
    @Composable
    get() = lightColorScheme(
        primary = colorResource(R.color.text),
        background = colorResource(R.color.background),
        surface = colorResource(R.color.background),
    )

val previewColorScheme: ColorScheme
    get() = lightColorScheme(
        primary = Color(0xFFE6EE9C),
        background = Color(0xFF303030),
        surface = Color(0xFF303030),
    )
