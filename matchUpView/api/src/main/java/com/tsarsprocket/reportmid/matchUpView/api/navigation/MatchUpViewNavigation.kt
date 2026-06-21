package com.tsarsprocket.reportmid.matchUpView.api.navigation

import com.tsarsprocket.reportmid.lol.api.domain.model.Region
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder

interface MatchUpViewNavigation {
    fun ViewStateHolder.showSummonerInfo(puuid: String, region: Region)

    companion object {
        const val TAG = "match_up_view_navigation_tag"
    }
}
