package com.tsarsprocket.reportmid.landing_impl.di

import com.tsarsprocket.reportmid.app_api.di.AppApi
import com.tsarsprocket.reportmid.base.di.PerApi
import com.tsarsprocket.reportmid.landing_api.di.LandingApi
import com.tsarsprocket.reportmid.view_state_api.di.StateReducersProvider
import com.tsarsprocket.reportmid.view_state_api.di.StateVisualizersProvider
import dagger.Component

@PerApi
@Component(
    dependencies = [
        AppApi::class,
    ],
    modules = [
        LandingModule::class,
    ]
)
internal interface LandingApiComponent : LandingApi, StateReducersProvider, StateVisualizersProvider {

    @Component.Factory
    interface Factory {

        fun create(appApi: AppApi): LandingApiComponent
    }
}
