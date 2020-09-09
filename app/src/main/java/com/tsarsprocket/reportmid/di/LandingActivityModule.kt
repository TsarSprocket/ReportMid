package com.tsarsprocket.reportmid.di

import com.tsarsprocket.reportmid.LandingActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class LandingActivityModule {

    @ContributesAndroidInjector
    abstract fun landingActivity(): LandingActivity
}