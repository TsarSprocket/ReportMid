package com.tsarsprocket.reportmid.lolServicesApi.di

import com.tsarsprocket.reportmid.lolServicesApi.riotapi.ServiceFactory

interface LolServicesApi {
    fun getServiceFactory(): ServiceFactory
}