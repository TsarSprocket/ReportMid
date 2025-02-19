package com.tsarsprocket.reportmid.findSummonerImpl.reducer

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.findSummonerApi.viewIntent.FindSummonerViewIntent
import com.tsarsprocket.reportmid.findSummonerImpl.R
import com.tsarsprocket.reportmid.findSummonerImpl.domain.FindSummonerUseCase
import com.tsarsprocket.reportmid.findSummonerImpl.viewEffect.ShowSnackViewEffect
import com.tsarsprocket.reportmid.findSummonerImpl.viewIntent.FindAndConfirmSummonerViewIntent
import com.tsarsprocket.reportmid.findSummonerImpl.viewState.ConfirmSummonerViewState
import com.tsarsprocket.reportmid.findSummonerImpl.viewState.SummonerDataEntryViewState
import com.tsarsprocket.reportmid.lol.model.GameName
import com.tsarsprocket.reportmid.lol.model.TagLine
import com.tsarsprocket.reportmid.viewStateApi.reducer.Reducer
import com.tsarsprocket.reportmid.viewStateApi.reducer.handleUnknownIntent
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewStateHolder
import javax.inject.Inject

@PerApi
internal class FindSummonerReducer @Inject constructor(
    private val useCase: FindSummonerUseCase,
) : Reducer {

    override suspend fun reduce(intent: ViewIntent, state: ViewState, stateHolder: ViewStateHolder): ViewState {
        return when(intent) {
            is FindAndConfirmSummonerViewIntent -> confirmFinding(intent, state, stateHolder)
            is FindSummonerViewIntent -> findSummoner(intent)
            else -> handleUnknownIntent(intent, state)
        }
    }

    private suspend fun confirmFinding(intent: FindAndConfirmSummonerViewIntent, state: ViewState, stateHolder: ViewStateHolder): ViewState {
        return try {
            val accountData = useCase.findAccount(GameName(intent.gameName), TagLine(intent.tagline), intent.region)

            if(accountData.isAlreadyInUse) {
                stateHolder.postEffect(ShowSnackViewEffect(R.string.snackSummonerIsAlreadyInUse))
                state
            } else {
                (state as SummonerDataEntryViewState).run {
                    ConfirmSummonerViewState(useCase.getSummonerData(accountData))
                }
            }
        } catch(exception: Exception) {
            stateHolder.postEffect(ShowSnackViewEffect(R.string.snackCannotFindSummoner))
            state
        }
    }

    private fun findSummoner(intent: FindSummonerViewIntent): SummonerDataEntryViewState = SummonerDataEntryViewState
}
