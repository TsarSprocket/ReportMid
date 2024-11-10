package com.tsarsprocket.reportmid.findSummonerApi.viewIntent

import com.tsarsprocket.reportmid.viewStateApi.viewIntent.CallViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ReturnIntentFactory

interface FindSummonerViewIntent : CallViewIntent<FindSummonerResult> {

    interface Factory {
        fun create(returnIntentFactory: ReturnIntentFactory<FindSummonerResult>, removeRecentState: Boolean): FindSummonerViewIntent
    }
}
