package com.tsarsprocket.reportmid.matchUpView.impl.reducer

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.kspApi.annotation.Reducer
import com.tsarsprocket.reportmid.lol.api.domain.model.Region
import com.tsarsprocket.reportmid.matchUpView.api.viewIntent.MatchUpIntent
import com.tsarsprocket.reportmid.matchUpView.impl.domain.Interactor
import com.tsarsprocket.reportmid.matchUpView.impl.domain.model.CurrentMatchUp
import com.tsarsprocket.reportmid.matchUpView.impl.domain.model.NoMatchUpFound
import com.tsarsprocket.reportmid.matchUpView.impl.viewIntent.LoadMatchUpIntent
import com.tsarsprocket.reportmid.matchUpView.impl.viewIntent.ParticipantAccountFailedToLoadIntent
import com.tsarsprocket.reportmid.matchUpView.impl.viewIntent.ParticipantAccountLoadedIntent
import com.tsarsprocket.reportmid.matchUpView.impl.viewState.ErrorState
import com.tsarsprocket.reportmid.matchUpView.impl.viewState.LoadingState
import com.tsarsprocket.reportmid.matchUpView.impl.viewState.MatchUpState
import com.tsarsprocket.reportmid.matchUpView.impl.viewState.NotFoundState
import com.tsarsprocket.reportmid.utils.common.logError
import com.tsarsprocket.reportmid.viewStateApi.reducer.ViewStateReducer
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewState.LoadablePart
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import kotlinx.coroutines.launch
import javax.inject.Inject

@PerApi
@Reducer(
    explicitIntents = [
        MatchUpIntent::class,
    ]
)
internal class Reducer @Inject constructor(
    private val interactor: Interactor,
    private val matchUpMapper: MatchUpMapper,
    private val accountMapper: AccountMapper,
) : ViewStateReducer {

    override suspend fun reduce(intent: ViewIntent, state: ViewState, stateHolder: ViewStateHolder): ViewState {
        return when(intent) {
            is MatchUpIntent -> LoadingState(intent.puuid, intent.region)
            is LoadMatchUpIntent -> loadMatchUp(intent, stateHolder)
            is ParticipantAccountLoadedIntent -> {
                val participant = (state as? MatchUpState)?.teams[intent.teamId]?.participants?.get(intent.puuid)
                if(participant != null) {
                    participant.account = LoadablePart.Loaded(intent.accountInfo)
                } else {
                    logError("Cannot find matchup participant (team=${intent.teamId}, puuid=${intent.puuid}) to set loaded account info to")
                }
                state
            }

            is ParticipantAccountFailedToLoadIntent -> {
                val participant = (state as? MatchUpState)?.teams[intent.teamId]?.participants?.get(intent.puuid)
                    if(participant != null) {
                        participant.account = LoadablePart.Failed
                    } else {
                        logError("Cannot find matchup participant (team=${intent.teamId}, puuid=${intent.puuid}) to mark account info as failed")
                    }
                state
            }

            else -> super.reduce(intent, state, stateHolder)
        }
    }

    private fun initiateAccountsLoading(matchUpState: MatchUpState, stateHolder: ViewStateHolder) {
        matchUpState.teams.forEach { (teamId, team) ->
            team.participants.keys.forEach { puuid ->
                startAccountLoadingForParticipant(teamId, puuid, matchUpState.region, stateHolder)
            }
        }
    }

    private fun startAccountLoadingForParticipant(
        teamId: Int,
        puuid: String,
        region: Region,
        stateHolder: ViewStateHolder,
    ) {
        stateHolder.viewHolderScope.launch {
            try {
                val account = interactor.getAccount(puuid, region)
                stateHolder.postIntent(
                    ParticipantAccountLoadedIntent(
                        accountInfo = accountMapper.map(account),
                        teamId = teamId,
                        puuid = puuid,
                    )
                )
            } catch(e: Exception) {
                logError("Exception while loading matchup participant (teamId=$teamId, puuid=$puuid) account", e)
                stateHolder.postIntent(
                    ParticipantAccountFailedToLoadIntent(
                        teamId = teamId,
                        puuid = puuid,
                    )
                )
            }
        }
    }

    private suspend fun loadMatchUp(intent: LoadMatchUpIntent, stateHolder: ViewStateHolder): ViewState {
        return try {
            when(val result = interactor.getCurrentMatchUp(intent.puuid, intent.region)) {
                is CurrentMatchUp -> {
                    val matchUpState = matchUpMapper.map(result, intent.puuid, intent.region) { teamId, puuid ->
                        startAccountLoadingForParticipant(teamId, puuid, intent.region, stateHolder)
                    }
                    initiateAccountsLoading(matchUpState, stateHolder)
                    matchUpState
                }

                NoMatchUpFound -> NotFoundState(intent.puuid, intent.region)
            }
        } catch(exception: Exception) {
            ErrorState(intent.puuid, intent.region, exception.message ?: "Unknown error")
        }
    }
}
