package com.tsarsprocket.reportmid.lol_services_impl.capability

import com.tsarsprocket.reportmid.app_api.capability.AppApi
import com.tsarsprocket.reportmid.base.capability.Capability
import com.tsarsprocket.reportmid.base.di.AppScope
import com.tsarsprocket.reportmid.lol_services_api.capability.LolServicesApi
import com.tsarsprocket.reportmid.lol_services_impl.di.DaggerLolServicesComponent
import com.tsarsprocket.reportmid.lol_services_impl.di.LolServicesComponent
import javax.inject.Inject
import javax.inject.Provider

@AppScope
class LolServicesCapability @Inject constructor(appApi: Provider<AppApi>) : Capability<LolServicesApi> {

    override val api: LolServicesApi = DaggerLolServicesComponent.factory().create(appApi.get()).also { component = it }

    companion object {
        internal lateinit var component: LolServicesComponent
    }
}