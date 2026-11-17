package com.tsarsprocket.reportmid.matchDetails.api.navigation

import com.tsarsprocket.reportmid.lol.api.domain.model.Region
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder

interface MatchDetailNavigation {

    fun ViewStateHolder.showProfileForSummoner(summonerPuuid: String, summonerRegion: Region)

    companion object {
        const val TAG = "ongoing_summoner_view_navigation"
    }
}