package com.tsarsprocket.reportmid.lol_services_api.riotapi

@Retention(AnnotationRetention.RUNTIME)
annotation class ServerInfo( val callBy: CallByType = CallByType.REGION, val baseServer: RiotServers = RiotServers.RIOT_API )
