package com.tsarsprocket.reportmid.tools

import kotlin.math.roundToInt

fun formatPoints(level: Int ) = if( level >= 10_000_000 ) "${( level.toFloat() / 1_000_000f ).roundToInt()}M"
    else if( level >= 10_000 ) "${(level.toFloat() / 1_000f ).roundToInt()}K"
        else level.toString()