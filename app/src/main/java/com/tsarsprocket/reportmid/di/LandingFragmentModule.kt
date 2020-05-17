package com.tsarsprocket.reportmid.di

import androidx.lifecycle.ViewModel
import com.tsarsprocket.reportmid.LandingFragment
import com.tsarsprocket.reportmid.LandingViewModel
import com.tsarsprocket.reportmid.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class LandingFragmentModule {

    @ContributesAndroidInjector
    abstract fun addLandingFragment(): LandingFragment

    @Binds
    @IntoMap
    @ViewModelKey( LandingViewModel::class )
    abstract fun bindViewModel( viewModel: LandingViewModel ): ViewModel
}