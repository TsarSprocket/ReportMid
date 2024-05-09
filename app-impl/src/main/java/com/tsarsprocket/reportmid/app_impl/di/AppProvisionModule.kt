package com.tsarsprocket.reportmid.app_impl.di

import com.tsarsprocket.reportmid.app_api.di.AppApi
import com.tsarsprocket.reportmid.app_impl.application.ReportMidApplication
import com.tsarsprocket.reportmid.base_api.di.AppScope
import dagger.Module
import dagger.Provides

@Module
interface AppProvisionModule {

    companion object {

        @Provides
        @AppScope
        fun provideAppApi(): AppApi = ReportMidApplication.theInstance.applicationComponent
    }
}