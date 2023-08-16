package com.tsarsprocket.reportmid.landing.di

import com.tsarsprocket.reportmid.base.di.Api
import com.tsarsprocket.reportmid.base.di.ApiKey
import com.tsarsprocket.reportmid.base.di.AppScope
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
interface LandingProvisionModule {

    @Binds
    @IntoMap
    @ApiKey(LandingApi::class)
    fun bindToApi(api: LandingApi): Api

    companion object {

        @Provides
        @AppScope
        fun provideLandingApi(): LandingApi = DaggerLandingApiComponent.create()
    }
}