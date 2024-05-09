package com.tsarsprocket.reportmid.app_impl.application

import android.app.Application
import com.tsarsprocket.reportmid.app_impl.di.AppApiComponent
import com.tsarsprocket.reportmid.app_impl.di.AppApiComponentLazyProxy
import com.tsarsprocket.reportmid.app_impl.di.DaggerAppApiComponent

class ReportMidApplication : Application() {

    internal val applicationComponent: AppApiComponent = AppApiComponentLazyProxy { DaggerAppApiComponent.factory().create() }

    init {
        theInstance = this
    }

    companion object {
        lateinit var theInstance: ReportMidApplication
    }
}