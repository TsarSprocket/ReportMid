package com.tsarsprocket.reportmid.landing_api.di

import com.tsarsprocket.reportmid.view_state_api.view_state.ViewIntent

interface LandingApi {
    fun getStartIntent(): ViewIntent
}
