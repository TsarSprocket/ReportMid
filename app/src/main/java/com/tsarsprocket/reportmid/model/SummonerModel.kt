package com.tsarsprocket.reportmid.model

import android.graphics.Bitmap
import com.merakianalytics.orianna.types.core.match.MatchHistory
import com.merakianalytics.orianna.types.core.summoner.Summoner

data class SummonerModel(
    val repository: Repository,
    val shadowSummoner: Summoner
) {

    val name: String = shadowSummoner.name
    val icon: Bitmap = shadowSummoner.profileIcon.image.get()
    val puuid: String = shadowSummoner.puuid
    val level: Int = shadowSummoner.level
    val masteries = List<ChampionMasteryModel>( shadowSummoner.championMasteries.size ) { i -> repository.getChampionMasteryModel(
        shadowSummoner.championMasteries[ i ]
    ) }
    val matchHistory = repository.getMatchHistoryModel( MatchHistory.forSummoner(shadowSummoner).get() )
}
