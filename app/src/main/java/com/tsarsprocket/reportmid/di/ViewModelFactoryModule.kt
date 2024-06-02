package com.tsarsprocket.reportmid.di

import androidx.lifecycle.ViewModel
import com.tsarsprocket.reportmid.baseApi.viewmodel.ViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Provider

@Module
internal class ViewModelFactoryModule {

    @Provides
    internal fun provideViewModel(
        creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
    ): ViewModelFactory {
        return ViewModelFactory(creators)
    }
}