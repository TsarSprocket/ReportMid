package com.tsarsprocket.reportmid.findSummonerImpl.viewIntent

import com.tsarsprocket.reportmid.findSummonerImpl.R
import com.tsarsprocket.reportmid.findSummonerImpl.di.FindSummonerCapabilityProvisionModule
import com.tsarsprocket.reportmid.findSummonerImpl.domain.FindSummonerUseCase
import com.tsarsprocket.reportmid.findSummonerImpl.viewEffect.ShowSnackViewEffect
import com.tsarsprocket.reportmid.findSummonerImpl.viewState.ConfirmSummonerViewState
import com.tsarsprocket.reportmid.findSummonerImpl.viewState.SummonerDataEntryViewState
import com.tsarsprocket.reportmid.lol.model.GameName
import com.tsarsprocket.reportmid.lol.model.Region
import com.tsarsprocket.reportmid.lol.model.TagLine
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewStateHolder
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@Parcelize
internal class FindAndConfirmSummonerViewIntent(
    private val gameName: String,
    private val tagline: String,
    private val region: Region,
) : ViewIntent {

    @IgnoredOnParcel
    @Inject
    lateinit var useCase: FindSummonerUseCase

    init {
        FindSummonerCapabilityProvisionModule.findSummonerCapabilityComponent.inject(this)
    }

    override suspend fun reduce(state: ViewState, stateHolder: ViewStateHolder): ViewState {
        return try {
            useCase.findAccount(GameName(gameName), TagLine(tagline), region).let { accountData ->
                if(accountData.isAlreadyInUse) {
                    stateHolder.postEffect(ShowSnackViewEffect(R.string.snackSummonerIsAlreadyInUse))
                    state
                } else {
                    (state as SummonerDataEntryViewState).run {
                        ConfirmSummonerViewState(useCase.getSummonerData(accountData), returnIntentFactory, removeRecentState)
                    }
                }
            }
        } catch(exception: Exception) {
            stateHolder.postEffect(ShowSnackViewEffect(R.string.snackCannotFindSummoner))
            state
        }
    }
}