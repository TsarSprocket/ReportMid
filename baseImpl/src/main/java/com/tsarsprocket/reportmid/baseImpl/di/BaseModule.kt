package com.tsarsprocket.reportmid.baseImpl.di

import androidx.fragment.app.FragmentFactory
import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.baseApi.viewmodel.ViewModelFactoryProvider
import com.tsarsprocket.reportmid.baseImpl.view.ReportMidFragmentFactory
import com.tsarsprocket.reportmid.baseImpl.viewmodel.ViewModelFactoryProviderImpl
import dagger.Binds
import dagger.Module

@Module
internal interface BaseModule {

    @Binds
    @PerApi
    fun bindReportMidFragmentFactory(fragmentFactory: ReportMidFragmentFactory): FragmentFactory

    @Binds
    @PerApi
    fun bindViewModelFactoryProvider(provider: ViewModelFactoryProviderImpl): ViewModelFactoryProvider

}