package com.tsarsprocket.reportmid.findSummonerApi.navigation

import com.tsarsprocket.reportmid.lol.model.Region
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder

interface FindSummonerNavigation {
    fun ViewStateHolder.returnCancel()
    fun ViewStateHolder.returnSuccess(puuid: String, region: Region)

    companion object {
        const val TAG = "find_summoner_navigation_tag"
    }
}