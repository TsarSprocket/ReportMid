package com.tsarsprocket.reportmid.landingApi.di

import com.tsarsprocket.reportmid.landingApi.viewIntent.LandingViewIntent

interface LandingApi {
    fun getLandingViewIntentCreator(): () -> LandingViewIntent
}
