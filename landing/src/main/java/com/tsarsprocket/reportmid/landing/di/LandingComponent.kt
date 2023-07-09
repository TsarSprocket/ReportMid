package com.tsarsprocket.reportmid.landing.di

import com.tsarsprocket.reportmid.base.capability.PerCapability
import com.tsarsprocket.reportmid.landing.capability.LandingApi
import com.tsarsprocket.reportmid.landing.view.LandingFragment
import dagger.Component

@PerCapability
@Component(
    modules = [
        LandingModule::class,
    ]
)
interface LandingComponent : LandingApi {
    fun inject(landingFragment: LandingFragment)
}
