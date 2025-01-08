package com.tsarsprocket.reportmid.profileScreenImpl.di

import com.tsarsprocket.reportmid.profileScreenApi.di.ProfileScreenApi
import com.tsarsprocket.reportmid.profileScreenImpl.viewIntent.ShowProfileScreenViewIntentImpl
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import dagger.Module
import dagger.Provides

@Module
internal class ProfileScreenModule {

    @Provides
    fun provideProfileScreenViewIntent(): () -> ViewIntent = { ShowProfileScreenViewIntentImpl() }

    @Provides
    fun provideProfileScreenApi(): ProfileScreenApi = ProfileScreenCapabilityProvisionModule.profileScreenCapabilityComponent
}