package com.tsarsprocket.reportmid.app.capability

import com.tsarsprocket.reportmid.app_api.capability.AppApi
import com.tsarsprocket.reportmid.base.capability.Capability
import com.tsarsprocket.reportmid.base.di.AppScope
import com.tsarsprocket.reportmid.app.di.AppComponent
import com.tsarsprocket.reportmid.app.di.DaggerAppComponent
import javax.inject.Inject

@AppScope
class AppCapability @Inject constructor() : Capability<AppApi> {

    override val api: AppApi
        get() = component

    companion object {
        internal val component: AppComponent by lazy<AppComponent> { DaggerAppComponent.create() }
    }
}
