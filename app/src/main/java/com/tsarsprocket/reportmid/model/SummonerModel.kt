package com.tsarsprocket.reportmid.model

import android.graphics.Bitmap
import com.merakianalytics.orianna.types.core.summoner.Summoner

data class SummonerModel(
    val shadowSummoner: Summoner
) {

    val name: String = shadowSummoner.name
    val icon: Bitmap = shadowSummoner.profileIcon.image.get()
    val puuid: String = shadowSummoner.puuid
}
