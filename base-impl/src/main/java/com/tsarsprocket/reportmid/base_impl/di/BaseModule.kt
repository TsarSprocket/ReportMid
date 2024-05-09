package com.tsarsprocket.reportmid.base_impl.di

import androidx.fragment.app.FragmentFactory
import com.tsarsprocket.reportmid.base_api.di.PerApi
import com.tsarsprocket.reportmid.base_impl.view.ReportMidFragmentFactory
import dagger.Binds
import dagger.Module

@Module
internal interface BaseModule {

    @Binds
    @PerApi
    fun bindReportMidFragmentFactory(fragmentFactory: ReportMidFragmentFactory): FragmentFactory
}