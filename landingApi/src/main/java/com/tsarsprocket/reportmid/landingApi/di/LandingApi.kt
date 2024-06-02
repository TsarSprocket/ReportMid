package com.tsarsprocket.reportmid.landingApi.di

import com.tsarsprocket.reportmid.viewStateApi.view_state.ViewIntent

interface LandingApi {
    fun getStartIntent(): ViewIntent
}
