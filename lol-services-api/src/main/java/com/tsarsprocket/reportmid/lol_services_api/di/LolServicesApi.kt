package com.tsarsprocket.reportmid.lol_services_api.di

import com.tsarsprocket.reportmid.lol_services_api.riotapi.ServiceFactory

interface LolServicesApi {
    fun getServiceFactory(): ServiceFactory
}