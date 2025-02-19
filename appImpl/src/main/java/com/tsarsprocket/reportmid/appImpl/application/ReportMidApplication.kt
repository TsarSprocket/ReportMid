package com.tsarsprocket.reportmid.appImpl.application

import android.app.Application
import com.tsarsprocket.reportmid.appImpl.di.AppApiComponentLazyProxy
import com.tsarsprocket.reportmid.appImpl.di.DaggerAppApiComponent
import kotlin.concurrent.Volatile

internal class ReportMidApplication : Application() {

    internal val applicationComponent = AppApiComponentLazyProxy { DaggerAppApiComponent.factory().create() }

    init {
        theInstance = this
        applicationComponent.forceInitialize()
    }

    companion object {
        @Volatile
        lateinit var theInstance: ReportMidApplication
    }
}