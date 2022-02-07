package com.tsarsprocket.reportmid.riotapi

@Retention(AnnotationRetention.RUNTIME)
annotation class ServerInfo( val callBy: CallByType = CallByType.REGION, val baseServer: RiotServers = RiotServers.RIOT_API )
