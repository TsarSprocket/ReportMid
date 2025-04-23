package com.tsarsprocket.reportmid.summonerViewImpl.viewState

import com.tsarsprocket.reportmid.kspApi.annotation.State
import com.tsarsprocket.reportmid.lol.model.Region
import com.tsarsprocket.reportmid.profileOverviewApi.viewIntent.ProfileOverviewViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import kotlinx.parcelize.Parcelize

@Parcelize
@State
data class SummonerViewState(
    val profileOverviewStateHolder: ViewStateHolder,
    val summonerPuuid: String,
    val summonerRegion: Region,
) : ViewState {

    override fun setParentHolder(parentHolder: ViewStateHolder) {
        profileOverviewStateHolder.setParentHolder(parentHolder)
    }

    override fun start() {
        profileOverviewStateHolder.apply {
            start()
            postIntent(ProfileOverviewViewIntent(summonerPuuid, summonerRegion))
        }
    }

    override fun stop() {
        profileOverviewStateHolder.stop()
    }
}