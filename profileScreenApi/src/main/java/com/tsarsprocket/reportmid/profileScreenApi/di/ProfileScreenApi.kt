package com.tsarsprocket.reportmid.profileScreenApi.di

import com.tsarsprocket.reportmid.profileScreenApi.viewIntent.ShowProfileScreenViewIntent

interface ProfileScreenApi {
    fun getShowProfileScreenViewIntentFactory(): () -> ShowProfileScreenViewIntent
}