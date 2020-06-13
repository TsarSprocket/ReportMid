package com.tsarsprocket.reportmid.model

import android.graphics.Bitmap
import com.google.common.collect.ImmutableList
import com.merakianalytics.orianna.types.core.summoner.Summoner

data class SummonerModel(
    val shadowSummoner: Summoner
) {

    val name: String = shadowSummoner.name
    val icon: Bitmap = shadowSummoner.profileIcon.image.get()
    val puuid: String = shadowSummoner.puuid
    val level: Int = shadowSummoner.level
    val masteries = List<ChampionMasteryModel>( shadowSummoner.championMasteries.size ) { i -> ChampionMasteryModel( shadowSummoner.championMasteries[ i ] ) }
}
