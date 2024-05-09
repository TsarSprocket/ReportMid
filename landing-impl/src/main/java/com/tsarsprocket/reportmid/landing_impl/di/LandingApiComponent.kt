package com.tsarsprocket.reportmid.landing_impl.di

import com.tsarsprocket.reportmid.app_api.di.AppApi
import com.tsarsprocket.reportmid.base_api.di.PerApi
import com.tsarsprocket.reportmid.landing_api.di.LandingApi
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
internal interface LandingApiComponent : LandingApi {

    @Component.Factory
    interface Factory {

        fun create(appApi: AppApi): LandingApiComponent
    }
}
