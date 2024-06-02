package com.tsarsprocket.reportmid.appImpl.di

import com.tsarsprocket.reportmid.appApi.di.AppApi
import com.tsarsprocket.reportmid.appImpl.application.ReportMidApplication
import com.tsarsprocket.reportmid.baseApi.di.AppScope
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