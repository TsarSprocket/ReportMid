package com.tsarsprocket.reportmid.findSummonerImpl.viewIntent

import com.tsarsprocket.reportmid.findSummonerApi.viewIntent.FindSummonerResult
import com.tsarsprocket.reportmid.findSummonerApi.viewIntent.FindSummonerViewIntent
import com.tsarsprocket.reportmid.findSummonerImpl.di.FindSummonerViewIntentFactory
import com.tsarsprocket.reportmid.findSummonerImpl.viewState.SummonerDataEntryViewState
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ReturnIntentFactory
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewStateHolder
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import javax.inject.Inject

internal class FindSummonerViewIntentImpl @AssistedInject constructor(
    @Assisted override var returnIntentProducer: ReturnIntentFactory<FindSummonerResult>,
    @Assisted override val removeRecentState: Boolean,
) : FindSummonerViewIntent {

    override suspend fun reduce(state: ViewState, stateHolder: ViewStateHolder): ViewState {
        return SummonerDataEntryViewState(returnIntentProducer, removeRecentState)
    }

    class Factory @Inject constructor(
        private val factory: FindSummonerViewIntentFactory,
    ) : FindSummonerViewIntent.Factory by factory
}
