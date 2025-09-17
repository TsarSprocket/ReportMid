package com.tsarsprocket.reportmid.matchHistory.impl.reducer

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.kspApi.annotation.Reducer
import com.tsarsprocket.reportmid.lol.api.model.Region
import com.tsarsprocket.reportmid.matchHistory.api.viewIntent.MatchHistoryIntent
import com.tsarsprocket.reportmid.matchHistory.impl.domain.interactor.MatchHistoryInteractor
import com.tsarsprocket.reportmid.matchHistory.impl.viewIntent.LoadMoreIntent
import com.tsarsprocket.reportmid.matchHistory.impl.viewIntent.ShowHistoryDataIntent
import com.tsarsprocket.reportmid.matchHistory.impl.viewState.InternalMatchHistoryState
import com.tsarsprocket.reportmid.matchHistory.impl.viewState.LoadingMatchHistoryState
import com.tsarsprocket.reportmid.matchHistory.impl.viewState.MatchInfo
import com.tsarsprocket.reportmid.matchHistory.impl.viewState.ShowingMatchHistoryState
import com.tsarsprocket.reportmid.viewStateApi.reducer.ViewStateReducer
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch
import javax.inject.Inject

@PerApi
@Reducer(
    explicitIntents = [
        MatchHistoryIntent::class,
    ]
)
internal class MatchHistoryReducer @Inject constructor(
    private val matchHistoryInteractor: MatchHistoryInteractor,
    private val matchDataMapper: MatchDataMapper,
) : ViewStateReducer {

    override suspend fun reduce(intent: ViewIntent, state: ViewState, stateHolder: ViewStateHolder): ViewState {
        return when {
            intent is MatchHistoryIntent -> stateHolder.loadMatchHistory(intent.puuid, intent.region)
            intent is ShowHistoryDataIntent && state is InternalMatchHistoryState -> showHistoryData(state.puuid, state.region, intent.listOfMatchInfos, intent.hasMoreToLoad)
            intent is LoadMoreIntent && state is ShowingMatchHistoryState -> stateHolder.loadMoreHistory(state)
            else -> super.reduce(intent, state, stateHolder)
        }
    }

    private fun ViewStateHolder.loadMatchHistory(puuid: String, region: Region): ViewState {
        viewHolderScope.launch {
            val data = matchHistoryInteractor.getMatchData(puuid, region, 0, CHUNK_SIZE)

            postIntent(ShowHistoryDataIntent(data.matches.map { matchDataMapper.map(it) }.toPersistentList(), data.hasMoreToLoad))
        }
        return LoadingMatchHistoryState(puuid, region)
    }

    private fun ViewStateHolder.loadMoreHistory(state: ShowingMatchHistoryState): ViewState {
        viewHolderScope.launch {
            val data = matchHistoryInteractor.getMatchData(state.puuid, state.region, state.matches.size, CHUNK_SIZE)

            postIntent(
                ShowHistoryDataIntent(
                    listOfMatchInfos = (state.matches + data.matches.map { matchDataMapper.map(it) }).toPersistentList(),
                    hasMoreToLoad = data.hasMoreToLoad,
                )
            )
        }

        return state.copy(
            isLoading = true,
        )
    }

    private fun showHistoryData(puuid: String, region: Region, listOfMatchInfos: ImmutableList<MatchInfo>, hasMoreToLoad: Boolean): ViewState {
        return ShowingMatchHistoryState(
            puuid = puuid,
            region = region,
            matches = listOfMatchInfos,
            isLoading = false,
            canLoadMore = hasMoreToLoad,
        )
    }

    private companion object {
        const val CHUNK_SIZE = 10
    }
}