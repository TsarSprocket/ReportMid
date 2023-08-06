package com.tsarsprocket.reportmid

import androidx.multidex.MultiDexApplication
import com.tsarsprocket.reportmid.base.di.Api
import com.tsarsprocket.reportmid.base.di.ApiLocator
import com.tsarsprocket.reportmid.di.DaggerReportMidAppComponent
import com.tsarsprocket.reportmid.di.ReportMidAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject
import javax.inject.Provider
import kotlin.reflect.KClass

class ReportMidApp : MultiDexApplication(), HasAndroidInjector, ApiLocator {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var apis: Map<Class<out Api>, @JvmSuppressWildcards Provider<Api>>

    val comp: ReportMidAppComponent = DaggerReportMidAppComponent.create()

    init {
        instance = this
    }

    override fun onCreate() {

        super.onCreate()

        comp.inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    override fun getApi(clazz: Class<out Api>): Api? = apis[clazz]?.get()

    companion object {
        lateinit var instance: ReportMidApp
    }
}

val applicationComponent: ReportMidAppComponent
    get() = ReportMidApp.instance.comp