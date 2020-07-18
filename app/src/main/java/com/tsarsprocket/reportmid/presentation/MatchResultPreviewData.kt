package com.tsarsprocket.reportmid.presentation

import android.graphics.Bitmap

data class MatchResultPreviewData (
    val mainChampionBitmap: Bitmap,
    val mainKills: Int,
    val mainDeaths: Int,
    val mainAssists: Int,
    val hasWon: Boolean,
    val creepScore: Int,
    val gameMode: String,
    val primaryRuneIconResId: Int,
    val secondaryRunePathIconResId: Int,
    val bmSummonerSpellD: Bitmap,
    val bmSummonerSpellF: Bitmap,
    val itemIcons: Array<Bitmap>
)