package com.tsarsprocket.reportmid.lol_services_api.riotapi

import com.tsarsprocket.reportmid.lol.model.Region

interface ServiceFactory {
    @Deprecated(message = "Use getService() extension instead", replaceWith = ReplaceWith("getService(region, clazz)"))
    fun getServiceUnchecked(region: Region, clazz: Class<*>): Any
}