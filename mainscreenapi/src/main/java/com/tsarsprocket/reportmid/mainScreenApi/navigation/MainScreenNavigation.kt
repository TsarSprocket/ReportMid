package com.tsarsprocket.reportmid.mainScreenApi.navigation

import com.tsarsprocket.reportmid.lol.api.model.Region
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder

interface MainScreenNavigation {

    fun ViewStateHolder.startSummonerView(puuid: String, region: Region)

    companion object {
        const val TAG = "main_screen_navigation_tag"
    }
}