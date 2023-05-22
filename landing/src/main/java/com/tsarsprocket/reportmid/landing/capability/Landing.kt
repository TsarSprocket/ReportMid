package com.tsarsprocket.reportmid.landing.capability

import com.tsarsprocket.reportmid.base.capability.Capability
import com.tsarsprocket.reportmid.landing.di.DaggerLandingComponent
import com.tsarsprocket.reportmid.landing.di.LandingComponent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Landing @Inject constructor() : Capability<LandingApi> {

    override val api: LandingApi by lazy { component }

    companion object {

        internal val component: LandingComponent by lazy { DaggerLandingComponent.create() }
    }
}