package com.tsarsprocket.reportmid.findSummonerApi.di

import com.tsarsprocket.reportmid.findSummonerApi.viewIntent.FindSummonerViewIntent

interface FindSummonerApi {
    fun getFindSummonerViewIntentFactory(): FindSummonerViewIntent.Factory
}