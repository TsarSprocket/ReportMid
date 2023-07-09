package com.tsarsprocket.reportmid.lol_services_api.riotapi

import com.tsarsprocket.reportmid.lol.model.Region

inline fun <reified T> ServiceFactory.getService(region: Region): T = getServiceUnchecked(region, T::class.java) as T
