package com.tsarsprocket.reportmid.landing.di

import com.tsarsprocket.reportmid.app_api.di.AppApi
import com.tsarsprocket.reportmid.base.di.PerApi
import com.tsarsprocket.reportmid.landing.view.LandingFragment
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
    fun inject(landingFragment: LandingFragment)

    @Component.Factory
    interface Factory {

        fun create(appApi: AppApi): LandingApiComponent
    }
}
