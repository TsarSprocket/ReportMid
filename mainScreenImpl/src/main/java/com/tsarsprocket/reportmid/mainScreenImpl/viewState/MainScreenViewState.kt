package com.tsarsprocket.reportmid.mainScreenImpl.viewState

import com.tsarsprocket.reportmid.kspApi.annotation.State
import com.tsarsprocket.reportmid.lol.api.model.Region
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import kotlinx.parcelize.Parcelize

@Parcelize
@State
class MainScreenViewState(
    val summonerStateHolder: ViewStateHolder,
    val initialSummonerPuuid: String,
    val initialSummonerRegion: Region,
) : ViewState {

    override fun setParentHolder(parentHolder: ViewStateHolder) {
        summonerStateHolder.setParentHolder(parentHolder)
    }

    override fun start() {
        summonerStateHolder.start()
    }

    override fun stop() {
        summonerStateHolder.stop()
    }
}
