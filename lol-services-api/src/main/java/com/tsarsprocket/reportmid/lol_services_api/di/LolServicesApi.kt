package com.tsarsprocket.reportmid.lol_services_api.di

import com.tsarsprocket.reportmid.base.di.Api
import com.tsarsprocket.reportmid.lol_services_api.riotapi.ServiceFactory

interface LolServicesApi : Api {
    fun getServiceFactory(): ServiceFactory
}