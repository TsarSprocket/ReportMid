package com.tsarsprocket.reportmid.matchUpView.impl.reducer

import com.tsarsprocket.reportmid.matchUpView.impl.domain.model.Summoner
import com.tsarsprocket.reportmid.matchUpView.impl.viewState.SummonerInfo
import javax.inject.Inject

internal class SummonerMapper @Inject constructor() {

    fun map(from: Summoner): SummonerInfo = SummonerInfo(level = from.summonerLevel.toString())
}
