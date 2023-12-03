package com.tsarsprocket.reportmid.landing_impl.di

import com.tsarsprocket.reportmid.app_api.di.AppApi
import com.tsarsprocket.reportmid.base.di.AppScope
import com.tsarsprocket.reportmid.landing_api.di.LandingApi
import dagger.Module
import dagger.Provides
import javax.inject.Provider

@Module
interface LandingProvisionModule {

    companion object {

        internal lateinit var landingApiComponent: LandingApiComponent
            private set

        @Provides
        @AppScope
        fun provideLandingApi(
            appApi: Provider<AppApi>,
        ): LandingApi = DaggerLandingApiComponent.factory().create(appApi.get()).also { landingApiComponent = it }
    }
}