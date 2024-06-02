package com.tsarsprocket.reportmid.landingImpl.di

import com.tsarsprocket.reportmid.appApi.di.AppApi
import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.landingApi.di.LandingApi
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
