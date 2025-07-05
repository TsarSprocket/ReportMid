package com.tsarsprocket.reportmid.matchData.impl.data

import com.tsarsprocket.reportmid.lolServicesApi.riotapi.ServiceFactory
import com.tsarsprocket.reportmid.lolServicesApi.riotapi.getService
import com.tsarsprocket.reportmid.matchData.impl.data.model.MatchIdPage
import com.tsarsprocket.reportmid.matchData.impl.data.model.MatchIdPageKey
import com.tsarsprocket.reportmid.matchData.impl.retrofit.MatchV5
import com.tsarsprocket.reportmid.requestManagerApi.data.Request
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

internal class MatchIdPageRequest @AssistedInject constructor(
    @Assisted key: MatchIdPageKey,
    private val serviceFactory: ServiceFactory,
) : Request<MatchIdPageKey, MatchIdPage>(key) {

    override suspend fun invoke() = MatchIdPage(
        serviceFactory.getService<MatchV5>(key.region)
            .getByPuuid(
                puuid = key.puuid,
                start = key.pageNo * PAGE_SIZE,
                count = PAGE_SIZE,
            )
    )

    @AssistedFactory
    interface Factory {
        fun createRequest(key: MatchIdPageKey): MatchIdPageRequest
    }
}