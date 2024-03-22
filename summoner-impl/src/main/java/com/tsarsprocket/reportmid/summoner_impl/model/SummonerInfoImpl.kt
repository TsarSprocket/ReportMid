package com.tsarsprocket.reportmid.summoner_impl.model

import com.tsarsprocket.reportmid.lol.model.Puuid
import com.tsarsprocket.reportmid.lol.model.Region
import com.tsarsprocket.reportmid.summoner_api.model.SummonerInfo
import com.tsarsprocket.reportmid.summoner_room.SummonerEntity

internal class SummonerInfoImpl(
    override val id: Long,
    override val puuid: Puuid,
    override val region: Region,
) : SummonerInfo {

    constructor(entity: SummonerEntity) : this(
        entity.id,
        Puuid(entity.puuid),
        Region.getById(entity.id)
    )
}