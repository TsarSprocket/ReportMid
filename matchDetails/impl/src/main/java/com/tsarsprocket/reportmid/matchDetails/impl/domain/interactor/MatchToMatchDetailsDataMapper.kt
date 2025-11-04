package com.tsarsprocket.reportmid.matchDetails.impl.domain.interactor

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.matchData.api.data.model.Match
import com.tsarsprocket.reportmid.matchDetails.impl.domain.model.MatchDetailsData
import javax.inject.Inject

@PerApi
internal class MatchToMatchDetailsDataMapper @Inject constructor() {

    fun map(match: Match): MatchDetailsData {
        TODO()
    }
}