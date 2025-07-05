package com.tsarsprocket.reportmid.lolServicesApi.riotapi

import com.tsarsprocket.reportmid.lol.api.model.Region

@Suppress("DEPRECATION")
inline fun <reified T> ServiceFactory.getService(region: Region): T = getServiceUnchecked(region, T::class.java) as T
