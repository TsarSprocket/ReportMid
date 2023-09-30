package com.tsarsprocket.reportmid.landing_impl.di

import com.tsarsprocket.reportmid.app_api.di.AppApi
import com.tsarsprocket.reportmid.base.di.Api
import com.tsarsprocket.reportmid.base.di.ApiKey
import com.tsarsprocket.reportmid.base.di.AppScope
import com.tsarsprocket.reportmid.landing_api.di.LandingApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Provider

@Module
interface LandingProvisionModule {

    @Binds
    @IntoMap
    @ApiKey(LandingApi::class)
    fun bindToApi(api: LandingApi): Api

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