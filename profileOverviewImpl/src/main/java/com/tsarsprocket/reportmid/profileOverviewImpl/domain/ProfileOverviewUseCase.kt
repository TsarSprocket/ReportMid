package com.tsarsprocket.reportmid.profileOverviewImpl.domain

import com.tsarsprocket.reportmid.lol.model.Region

internal interface ProfileOverviewUseCase {
    suspend fun getOverview(puuid: String, region: Region): ProfileOverviewModel
}