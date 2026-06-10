package com.tsarsprocket.reportmid.matchUpView.impl.reducer

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.kspApi.annotation.Reducer
import com.tsarsprocket.reportmid.lol.api.domain.model.Region
import com.tsarsprocket.reportmid.matchUpView.api.viewIntent.MatchUpIntent
import com.tsarsprocket.reportmid.matchUpView.impl.domain.Interactor
import com.tsarsprocket.reportmid.matchUpView.impl.domain.model.CurrentMatchUp
import com.tsarsprocket.reportmid.matchUpView.impl.domain.model.NoMatchUpFound
import com.tsarsprocket.reportmid.matchUpView.impl.viewIntent.InternalIntent
import com.tsarsprocket.reportmid.matchUpView.impl.viewIntent.LoadMatchUpIntent
import com.tsarsprocket.reportmid.matchUpView.impl.viewIntent.ParticipantAccountFailedToLoadIntent
import com.tsarsprocket.reportmid.matchUpView.impl.viewIntent.ParticipantAccountLoadedIntent
import com.tsarsprocket.reportmid.matchUpView.impl.viewIntent.StartLoadingParticipantAccountIntent
import com.tsarsprocket.reportmid.matchUpView.impl.viewIntent.StartLoadingSummonerInfoIntent
import com.tsarsprocket.reportmid.matchUpView.impl.viewIntent.SummonerInfoFailedToLoadIntent
import com.tsarsprocket.reportmid.matchUpView.impl.viewIntent.SummonerInfoLoadedIntent
import com.tsarsprocket.reportmid.matchUpView.impl.viewState.ErrorState
import com.tsarsprocket.reportmid.matchUpView.impl.viewState.KnownPlayerInfo
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
    private val summonerMapper: SummonerMapper,
) : ViewStateReducer {

    override suspend fun reduce(intent: ViewIntent, state: ViewState, stateHolder: ViewStateHolder): ViewState {
        return when (intent ) {
            is MatchUpIntent -> LoadingState(intent.puuid, intent.region)

            is InternalIntent -> when(intent) {
                is LoadMatchUpIntent -> loadMatchUp(intent, stateHolder)

                is StartLoadingParticipantAccountIntent -> with (intent) {
                    startAccountLoadingForParticipant(teamId, puuid, region, stateHolder)
                    state
                }

                is ParticipantAccountLoadedIntent -> {
                    val player = (state as? MatchUpState)?.teams[intent.teamId]?.participants?.get(intent.puuid)?.player as? KnownPlayerInfo
                    if(player != null) {
                        player.account = LoadablePart.Loaded(intent.accountInfo)
                    } else {
                        logError("Cannot find matchup participant (team=${intent.teamId}, puuid=${intent.puuid}) to set loaded account info to")
                    }
                    state
                }

                is ParticipantAccountFailedToLoadIntent -> {
                    val player = (state as? MatchUpState)?.teams[intent.teamId]?.participants?.get(intent.puuid)?.player as? KnownPlayerInfo
                    if(player != null) {
                        player.account = LoadablePart.Failed
                    } else {
                        logError("Cannot find matchup participant (team=${intent.teamId}, puuid=${intent.puuid}) to mark account info as failed")
                    }
                    state
                }

                is StartLoadingSummonerInfoIntent -> with(intent) {
                    startSummonerInfoLoadingForParticipant(teamId, puuid, region, stateHolder)
                    state
                }

                is SummonerInfoLoadedIntent -> {
                    val player = (state as? MatchUpState)?.teams[intent.teamId]?.participants?.get(intent.puuid)?.player as? KnownPlayerInfo
                    if(player != null) {
                        player.summoner = LoadablePart.Loaded(intent.summonerInfo)
                    } else {
                        logError("Cannot find matchup participant (team=${intent.teamId}, puuid=${intent.puuid}) to set loaded summoner info to")
                    }
                    state
                }

                is SummonerInfoFailedToLoadIntent -> {
                    val player = (state as? MatchUpState)?.teams[intent.teamId]?.participants?.get(intent.puuid)?.player as? KnownPlayerInfo
                    if(player != null) {
                        player.summoner = LoadablePart.Failed
                    } else {
                        logError("Cannot find matchup participant (team=${intent.teamId}, puuid=${intent.puuid}) to mark summoner info as failed")
                    }
                    state
                }
            }

            else ->super.reduce(intent, state, stateHolder)
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

    private fun startSummonerInfoLoadingForParticipant(
        teamId: Int,
        puuid: String,
        region: Region,
        stateHolder: ViewStateHolder,
    ) {
        stateHolder.viewHolderScope.launch {
            try {
                val summoner = interactor.getSummoner(puuid, region)
                stateHolder.postIntent(
                    SummonerInfoLoadedIntent(
                        summonerInfo = summonerMapper.map(summoner),
                        teamId = teamId,
                        puuid = puuid,
                    )
                )
            } catch(e: Exception) {
                logError("Exception while loading matchup participant (teamId=$teamId, puuid=$puuid) summoner info", e)
                stateHolder.postIntent(
                    SummonerInfoFailedToLoadIntent(
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
                    val matchUpState = matchUpMapper.map(
                        result,
                        intent.puuid,
                        intent.region,
                        accountLoadTrigger = { teamId, puuid ->
                            startAccountLoadingForParticipant(teamId, puuid, intent.region, stateHolder)
                        },
                        summonerLoadTrigger = { teamId, puuid ->
                            startSummonerInfoLoadingForParticipant(teamId, puuid, intent.region, stateHolder)
                        },
                    )
                    matchUpState
                }

                NoMatchUpFound -> NotFoundState(intent.puuid, intent.region)
            }
        } catch(exception: Exception) {
            logError("Error loading matchup for puuid=${intent.puuid}, region=${intent.region}", exception)
            ErrorState(intent.puuid, intent.region, exception.message ?: "Unknown error")
        }
    }
}
