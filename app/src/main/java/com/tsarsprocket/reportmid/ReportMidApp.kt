package com.tsarsprocket.reportmid

import android.app.Application
import androidx.multidex.MultiDexApplication
import com.google.common.io.CharSource
import com.merakianalytics.orianna.Orianna
import com.tsarsprocket.reportmid.di.DaggerReportMidAppComponent
import com.tsarsprocket.reportmid.di.ReportMidAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class ReportMidApp: MultiDexApplication(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    val comp: ReportMidAppComponent = DaggerReportMidAppComponent.create()

    override fun onCreate() {

        super.onCreate()

        comp.inject( this )

        Orianna.loadConfiguration( CharSource.wrap( loadOriannaConfigToString() ) )
    }

    private fun loadOriannaConfigToString(): String {

        resources.openRawResource( R.raw.orianna_config )
        TODO("Not yet implemented")
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector
}