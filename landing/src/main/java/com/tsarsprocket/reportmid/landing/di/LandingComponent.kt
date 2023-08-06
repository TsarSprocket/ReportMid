package com.tsarsprocket.reportmid.landing.di

import com.tsarsprocket.reportmid.base.di.PerApi
import com.tsarsprocket.reportmid.landing.view.LandingFragment
import dagger.Component

@PerApi
@Component(
    modules = [
        LandingModule::class,
    ]
)
internal interface LandingComponent : LandingApi {
    fun inject(landingFragment: LandingFragment)
}
