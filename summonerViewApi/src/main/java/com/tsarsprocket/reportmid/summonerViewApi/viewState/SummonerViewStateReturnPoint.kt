package com.tsarsprocket.reportmid.summonerViewApi.viewState

import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent

/**
 * Marker interface used when navigation from summoner's sub-states
 */
interface SummonerViewStateReturnPoint {
    fun getReturnIntent(): ViewIntent
}