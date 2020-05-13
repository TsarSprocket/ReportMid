package com.tsarsprocket.reportmid.di

import com.tsarsprocket.reportmid.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {

    @ContributesAndroidInjector
    abstract fun mainActivity(): MainActivity
}