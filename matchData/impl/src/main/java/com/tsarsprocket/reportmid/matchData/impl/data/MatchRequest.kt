package com.tsarsprocket.reportmid.matchData.impl.data

import com.tsarsprocket.reportmid.lol.api.model.Region
import com.tsarsprocket.reportmid.lolServicesApi.riotapi.ServiceFactory
import com.tsarsprocket.reportmid.lolServicesApi.riotapi.getService
import com.tsarsprocket.reportmid.matchData.impl.data.model.MatchKey
import com.tsarsprocket.reportmid.matchData.impl.retrofit.MatchDto
import com.tsarsprocket.reportmid.matchData.impl.retrofit.MatchV5
import com.tsarsprocket.reportmid.requestManagerApi.data.Request
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

internal class MatchRequest @AssistedInject constructor(
    @Assisted matchId: String,
    @Assisted private val region: Region,
    private val serviceFactory: ServiceFactory,
) : Request<MatchKey, MatchDto>(MatchKey(matchId)) {

    override suspend fun invoke(): MatchDto = serviceFactory.getService<MatchV5>(region).getByMatchId(key.matchId)

    @AssistedFactory
    interface Factory {
        fun createRequest(matchId: String, region: Region): MatchRequest
    }
}