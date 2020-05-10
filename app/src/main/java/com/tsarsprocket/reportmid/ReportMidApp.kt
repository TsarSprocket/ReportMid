package com.tsarsprocket.reportmid

import android.app.Application
import com.tsarsprocket.reportmid.di.DaggerReportMidAppComponent
import com.tsarsprocket.reportmid.di.ReportMidAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class ReportMidApp: Application(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    val comp: ReportMidAppComponent = DaggerReportMidAppComponent.create()

    override fun onCreate() {

        super.onCreate()

        comp.inject( this )
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector
}