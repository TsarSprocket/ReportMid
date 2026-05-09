package com.tsarsprocket.reportmid.matchUpView.impl.domain

import com.tsarsprocket.reportmid.lol.api.domain.model.Region
import com.tsarsprocket.reportmid.matchUpView.impl.domain.model.MatchUpDomainModel

internal interface Interactor {
    suspend fun getCurrentMatchUp(puuid: String, region: Region): MatchUpDomainModel
}