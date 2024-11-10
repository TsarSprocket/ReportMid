package com.tsarsprocket.reportmid.landingApi.di

import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent

interface LandingApi {
    fun getStartIntent(): ViewIntent
}
