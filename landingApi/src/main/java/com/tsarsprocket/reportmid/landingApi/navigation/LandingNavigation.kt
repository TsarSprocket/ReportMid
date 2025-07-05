package com.tsarsprocket.reportmid.landingApi.navigation

import com.tsarsprocket.reportmid.lol.api.model.Puuid
import com.tsarsprocket.reportmid.lol.api.model.Region
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder

interface LandingNavigation {

    fun ViewStateHolder.findSummoner()

    fun ViewStateHolder.proceed(puuid: Puuid, region: Region)

    companion object {
        const val TAG = "landing_navigation_tag"
    }
}