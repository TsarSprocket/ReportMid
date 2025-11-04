package com.tsarsprocket.reportmid.matchDetails.impl.reducer

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.matchDetails.impl.domain.model.MatchDetailsData
import com.tsarsprocket.reportmid.matchDetails.impl.viewState.MatchDetailsState
import javax.inject.Inject

@PerApi
internal class MatchDetailsDataToStateMapper @Inject constructor() {

    fun map(data: MatchDetailsData): MatchDetailsState {
        TODO()
    }
}