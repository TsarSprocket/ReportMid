package com.tsarsprocket.reportmid.landing.di

import com.tsarsprocket.reportmid.landing.view.LandingFragment
import dagger.Component

@Component(
    modules = [
        LandingModule::class,
    ]
)
interface LandingComponent {
    fun inject(fragment: LandingFragment)
}