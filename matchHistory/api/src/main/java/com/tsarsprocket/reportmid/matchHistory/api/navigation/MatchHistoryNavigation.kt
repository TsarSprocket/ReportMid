package com.tsarsprocket.reportmid.matchHistory.api.navigation

import com.tsarsprocket.reportmid.lol.api.domain.model.Region
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder

interface MatchHistoryNavigation {

    fun ViewStateHolder.showMatchDetails(region: Region, matchId: String)

    companion object {
        const val TAG = "match_history_navigation_tag"
    }
}