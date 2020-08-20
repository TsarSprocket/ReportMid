package com.tsarsprocket.reportmid.presentation

import android.graphics.Bitmap

data class PlayerPresentation(
    val championIcon: Bitmap,
    val summonerChampionSkill: Int,
    val summonerName: String,
    val summonerLevel: Int,
    val soloqueueRank: String,
    val soloqueueWinrate: Float,
    val summonerSpellD: Bitmap,
    val summonerSpellF: Bitmap,
    val primaryRunePathIconResId: Int,
    val secondaryRunePathIconResIs: Int
)