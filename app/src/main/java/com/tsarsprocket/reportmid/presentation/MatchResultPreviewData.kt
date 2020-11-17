package com.tsarsprocket.reportmid.presentation

import android.graphics.Bitmap
import android.graphics.drawable.Drawable

data class MatchResultPreviewData (
    val mainChampionBitmap: Bitmap,
    val mainKills: Int,
    val mainDeaths: Int,
    val mainAssists: Int,
    val hasWon: Boolean,
    val remake: Boolean,
    val creepScore: Int,
    val gameModeNameResId: Int,
    val primaryRuneIconResId: Int?,
    val secondaryRunePathIconResId: Int?,
    val bmSummonerSpellD: Bitmap,
    val bmSummonerSpellF: Bitmap,
    val itemIcons: Array<Drawable>
)