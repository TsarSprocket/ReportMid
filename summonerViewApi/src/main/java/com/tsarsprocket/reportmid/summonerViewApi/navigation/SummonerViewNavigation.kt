package com.tsarsprocket.reportmid.summonerViewApi.navigation

import com.tsarsprocket.reportmid.lol.model.Region
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder

interface SummonerViewNavigation {

    fun ViewStateHolder.startProfileOverview(summonerPuuid: String, summonerRegion: Region)

    companion object {
        const val TAG = "summoner_view_navigation_tag"
    }
}