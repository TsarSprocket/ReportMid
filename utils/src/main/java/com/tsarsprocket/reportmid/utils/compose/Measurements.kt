package com.tsarsprocket.reportmid.utils.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp


@Stable
val Float.pxToDp: Dp
    @Composable get() = this.let { with(LocalDensity.current) { it.toDp() } }
