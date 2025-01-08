package com.tsarsprocket.reportmid.appImpl.application

import android.app.Application
import com.tsarsprocket.reportmid.appImpl.di.AppApiComponent
import com.tsarsprocket.reportmid.appImpl.di.AppApiComponentLazyProxy
import com.tsarsprocket.reportmid.appImpl.di.DaggerAppApiComponent

internal class ReportMidApplication : Application() {

    internal val applicationComponent: AppApiComponent = AppApiComponentLazyProxy { DaggerAppApiComponent.factory().create() }

    init {
        applicationComponent.getAppContext() // To ensure creation of the component
        theInstance = this
    }

    companion object {
        lateinit var theInstance: ReportMidApplication
    }
}