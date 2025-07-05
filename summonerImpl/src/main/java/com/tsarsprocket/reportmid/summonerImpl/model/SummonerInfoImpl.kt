package com.tsarsprocket.reportmid.summonerImpl.model

import com.tsarsprocket.reportmid.lol.api.model.Puuid
import com.tsarsprocket.reportmid.lol.api.model.Region
import com.tsarsprocket.reportmid.summonerApi.model.SummonerInfo
import com.tsarsprocket.reportmid.summonerRoom.SummonerEntity

internal class SummonerInfoImpl(
    override val id: Long,
    override val puuid: Puuid,
    override val region: Region,
) : SummonerInfo {

    constructor(entity: SummonerEntity) : this(
        entity.id,
        Puuid(entity.puuid),
        Region.getById(entity.regionId)
    )
}