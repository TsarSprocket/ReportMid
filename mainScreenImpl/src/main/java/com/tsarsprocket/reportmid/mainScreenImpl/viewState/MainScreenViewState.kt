package com.tsarsprocket.reportmid.mainScreenImpl.viewState

import com.tsarsprocket.reportmid.kspApi.annotation.State
import com.tsarsprocket.reportmid.lol.model.Region
import com.tsarsprocket.reportmid.summonerViewApi.viewIntent.SummonerViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import kotlinx.parcelize.Parcelize

@Parcelize
@State
class MainScreenViewState(
    val summonerStateHolder: ViewStateHolder,
    private val initialSummonerPuuid: String,
    private val initialSummonerRegion: Region,
) : ViewState {

    override fun setParentHolder(parentHolder: ViewStateHolder) {
        summonerStateHolder.setParentHolder(parentHolder)
    }

    override fun start() {
        summonerStateHolder.apply {
            start()
            postIntent(SummonerViewIntent(initialSummonerPuuid, initialSummonerRegion))
        }
    }

    override fun stop() {
        summonerStateHolder.stop()
    }
}
