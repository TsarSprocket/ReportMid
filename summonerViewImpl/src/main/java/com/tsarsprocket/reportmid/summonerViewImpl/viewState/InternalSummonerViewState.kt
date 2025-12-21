package com.tsarsprocket.reportmid.summonerViewImpl.viewState

import com.tsarsprocket.reportmid.kspApi.annotation.State
import com.tsarsprocket.reportmid.lol.api.domain.model.Region
import com.tsarsprocket.reportmid.matchHistory.api.viewIntent.MatchHistoryIntent
import com.tsarsprocket.reportmid.profileOverviewApi.viewIntent.ProfileOverviewViewIntent
import com.tsarsprocket.reportmid.summonerViewApi.viewState.SummonerViewStateReturnPoint
import com.tsarsprocket.reportmid.summonerViewImpl.viewIntent.ReturnToSummoner
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.LazyViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import kotlinx.parcelize.Parcelize

@Parcelize
@State
internal data class InternalSummonerViewState(
    val profileOverviewStateHolder: ViewStateHolder,
    val matchHistoryStateHolder: ViewStateHolder,
    val summonerPuuid: String,
    val summonerRegion: Region,
    val activePage: ActivePage,
) : ViewState, SummonerViewStateReturnPoint {

    override fun getRestoreStateIntent(): ViewIntent = ReturnToSummoner(
        puuid = summonerPuuid,
        region = summonerRegion,
        activePage = activePage,
    )

    override fun setParentHolder(parentHolder: ViewStateHolder) {
        profileOverviewStateHolder.setParentHolder(parentHolder)
        matchHistoryStateHolder.setParentHolder(parentHolder)
    }

    override fun start() {
        profileOverviewStateHolder.apply {
            start()
            postIntent(ProfileOverviewViewIntent(summonerPuuid, summonerRegion))
        }
        matchHistoryStateHolder.apply {
            start()
            postIntent(LazyViewIntent(MatchHistoryIntent(summonerPuuid, summonerRegion)))
        }
    }

    override fun stop() {
        profileOverviewStateHolder.stop()
        matchHistoryStateHolder.stop()
    }
}