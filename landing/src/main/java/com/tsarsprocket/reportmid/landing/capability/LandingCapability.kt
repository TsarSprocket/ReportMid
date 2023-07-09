package com.tsarsprocket.reportmid.landing.capability

import com.tsarsprocket.reportmid.base.capability.Capability
import com.tsarsprocket.reportmid.base.di.AppScope
import com.tsarsprocket.reportmid.landing.di.DaggerLandingComponent
import com.tsarsprocket.reportmid.landing.di.LandingComponent
import javax.inject.Inject

@AppScope
class LandingCapability @Inject constructor() : Capability<LandingApi> {

    override val api: LandingApi
        get() = component

    companion object {
        internal val component: LandingComponent by lazy { DaggerLandingComponent.create() }
    }
}