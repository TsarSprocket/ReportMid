package com.tsarsprocket.reportmid.model

import android.graphics.Bitmap
import com.merakianalytics.orianna.types.core.summoner.Summoner

data class Summoner(
    val name: String,
    val icon: Bitmap,
    val shadowSummoner: Summoner
)
