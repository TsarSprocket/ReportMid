package com.tsarsprocket.reportmid.model

import android.graphics.Bitmap

data class MatchResultPreviewData (
    val mainChampionBitmap: Bitmap,
    val mainKills: Int,
    val mainDeaths: Int,
    val mainAssists: Int,
    val teamKills: Int,
    val teamDeaths: Int,
    val teamAssists: Int,
    val hasWon: Boolean,
    val blueTeamIcons: Array<Bitmap>,
    val redTeamIcons: Array<Bitmap>
) {
    val teamsIcons = arrayOf( blueTeamIcons, redTeamIcons )
}