package com.tsarsprocket.reportmid.findSummonerImpl.reducer

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.findSummonerApi.viewIntent.FindSummonerViewIntent
import com.tsarsprocket.reportmid.findSummonerImpl.R
import com.tsarsprocket.reportmid.findSummonerImpl.domain.FindSummonerUseCase
import com.tsarsprocket.reportmid.findSummonerImpl.viewEffect.ShowSnackViewEffect
import com.tsarsprocket.reportmid.findSummonerImpl.viewIntent.InternalViewIntent
import com.tsarsprocket.reportmid.findSummonerImpl.viewIntent.InternalViewIntent.FindAndConfirmSummonerViewIntent
import com.tsarsprocket.reportmid.findSummonerImpl.viewIntent.InternalViewIntent.GameNameChanged
import com.tsarsprocket.reportmid.findSummonerImpl.viewIntent.InternalViewIntent.RegionSelected
import com.tsarsprocket.reportmid.findSummonerImpl.viewIntent.InternalViewIntent.TagLineChanged
import com.tsarsprocket.reportmid.findSummonerImpl.viewState.ConfirmSummonerViewState
import com.tsarsprocket.reportmid.findSummonerImpl.viewState.SummonerDataEntryViewState
import com.tsarsprocket.reportmid.kspApi.annotation.Reducer
import com.tsarsprocket.reportmid.lol.model.GameName
import com.tsarsprocket.reportmid.lol.model.TagLine
import com.tsarsprocket.reportmid.utils.common.logError
import com.tsarsprocket.reportmid.viewStateApi.reducer.ViewStateReducer
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewStateHolder
import javax.inject.Inject

@PerApi
@Reducer(
    explicitIntents = [
        FindSummonerViewIntent::class,
    ],
)
internal class FindSummonerReducer @Inject constructor(
    private val useCase: FindSummonerUseCase,
) : ViewStateReducer {

    override suspend fun reduce(intent: ViewIntent, state: ViewState, stateHolder: ViewStateHolder): ViewState = when(intent) {
        is InternalViewIntent -> {
            when(intent) {
                is FindAndConfirmSummonerViewIntent -> confirmFinding(intent, state, stateHolder)
                is GameNameChanged -> (state as SummonerDataEntryViewState).copy(gameName = GameName(intent.newGameName))
                is TagLineChanged -> (state as SummonerDataEntryViewState).copy(tagLine = TagLine(intent.newTagline))
                is RegionSelected -> (state as SummonerDataEntryViewState).copy(selectedRegionId = intent.newRegionId)
            }
        }

        is FindSummonerViewIntent -> SummonerDataEntryViewState()
        else -> super.reduce(intent, state, stateHolder)
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
            logError(exception.localizedMessage.orEmpty(), exception)
            stateHolder.postEffect(ShowSnackViewEffect(R.string.snackCannotFindSummoner))
            state
        }
    }
}
