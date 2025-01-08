package com.tsarsprocket.reportmid.findSummonerImpl.viewIntent

import com.tsarsprocket.reportmid.findSummonerApi.viewIntent.FindSummonerResult
import com.tsarsprocket.reportmid.findSummonerApi.viewIntent.FindSummonerViewIntent
import com.tsarsprocket.reportmid.findSummonerImpl.di.FindSummonerViewIntentFactory
import com.tsarsprocket.reportmid.findSummonerImpl.viewState.SummonerDataEntryViewState
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ReturnIntentFactory
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewStateHolder
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@Parcelize
internal class FindSummonerViewIntentImpl(
    override var returnIntentProducer: ReturnIntentFactory<FindSummonerResult>,
    override val removeRecentState: Boolean,
) : FindSummonerViewIntent {

    override suspend fun reduce(state: ViewState, stateHolder: ViewStateHolder): ViewState {
        return SummonerDataEntryViewState(returnIntentProducer, removeRecentState)
    }

    class Factory @Inject constructor(
        private val factory: FindSummonerViewIntentFactory,
    ) : FindSummonerViewIntent.Factory by factory
}
