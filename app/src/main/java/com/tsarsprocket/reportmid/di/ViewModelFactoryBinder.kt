package com.tsarsprocket.reportmid.di

import androidx.lifecycle.ViewModelProvider
import com.tsarsprocket.reportmid.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
internal abstract class ViewModelFactoryBinder {

    @Binds
    internal abstract fun bindViewModel( vmf: ViewModelFactory) : ViewModelProvider.Factory
}