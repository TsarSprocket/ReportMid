package com.tsarsprocket.reportmid.baseImpl.di

import androidx.fragment.app.FragmentFactory
import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.baseImpl.view.ReportMidFragmentFactory
import dagger.Binds
import dagger.Module

@Module
internal interface BaseModule {

    @Binds
    @PerApi
    fun bindReportMidFragmentFactory(fragmentFactory: ReportMidFragmentFactory): FragmentFactory
}