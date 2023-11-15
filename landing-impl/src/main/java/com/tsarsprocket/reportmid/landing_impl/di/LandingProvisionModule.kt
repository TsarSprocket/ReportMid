package com.tsarsprocket.reportmid.landing_impl.di

import com.tsarsprocket.reportmid.app_api.di.AppApi
import com.tsarsprocket.reportmid.base.di.AppScope
import com.tsarsprocket.reportmid.base.di.BindingExport
import com.tsarsprocket.reportmid.landing_api.di.LandingApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import javax.inject.Provider

@Module
interface LandingProvisionModule {

    @Binds
    @IntoSet
    @AppScope
    @BindingExport
    fun bindToApi(api: LandingApi): Any

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