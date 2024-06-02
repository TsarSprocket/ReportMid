package com.tsarsprocket.reportmid.landingImpl.di

import com.tsarsprocket.reportmid.appApi.di.AppApi
import com.tsarsprocket.reportmid.baseApi.di.AppScope
import com.tsarsprocket.reportmid.landingApi.di.LandingApi
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