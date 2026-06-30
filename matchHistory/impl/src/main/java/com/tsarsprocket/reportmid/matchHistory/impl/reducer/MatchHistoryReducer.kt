package com.tsarsprocket.reportmid.matchHistory.impl.reducer

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.kspApi.annotation.Reducer
import com.tsarsprocket.reportmid.lol.api.domain.model.Region
import com.tsarsprocket.reportmid.matchHistory.api.viewIntent.MatchHistoryIntent
import com.tsarsprocket.reportmid.matchHistory.impl.domain.interactor.MatchHistoryInteractor
import com.tsarsprocket.reportmid.matchHistory.impl.viewIntent.LoadMoreIntent
import com.tsarsprocket.reportmid.matchHistory.impl.viewIntent.ShowHistoryDataIntent
import com.tsarsprocket.reportmid.matchHistory.impl.viewState.InternalMatchHistoryState
import com.tsarsprocket.reportmid.matchHistory.impl.viewState.ItemToShow
import com.tsarsprocket.reportmid.matchHistory.impl.viewState.LoadingMatchHistoryState
import com.tsarsprocket.reportmid.matchHistory.impl.viewState.ShowingMatchHistoryState
import com.tsarsprocket.reportmid.utils.common.logError
import com.tsarsprocket.reportmid.viewStateApi.reducer.ViewStateReducer
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

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
        return when(intent) {
            is MatchHistoryIntent -> stateHolder.loadMatchHistory(intent.puuid, intent.region)
            is ShowHistoryDataIntent if state is InternalMatchHistoryState -> showHistoryData(
                puuid = state.puuid,
                region = state.region,
                lastLoadedAt = (state as? ShowingMatchHistoryState)?.lastLoadedAt ?: System.currentTimeMillis(),
                items = intent.items,
                hasMoreToLoad = intent.hasMoreToLoad,
            )

            is LoadMoreIntent if state is ShowingMatchHistoryState -> stateHolder.loadMoreHistory(state)
            else -> super.reduce(intent, state, stateHolder)
        }
    }

    private fun ViewStateHolder.loadMatchHistory(puuid: String, region: Region): ViewState {
        viewHolderScope.launch {
            try {
                val data = matchHistoryInteractor.getMatchData(puuid, region, 0, CHUNK_SIZE)

                postIntent(ShowHistoryDataIntent(
                    items = data.matches.map { matchDataMapper.map(it) }.toPersistentList(),
                    hasMoreToLoad = data.hasMoreToLoad,
                ))
            } catch(ex: CancellationException) {
                throw ex
            } catch(ex: Exception) {
                logError("Error loading matches", ex)
                // TODO: Implement failure-to-reload state
            }
        }
        return LoadingMatchHistoryState(puuid, region)
    }

    private fun ViewStateHolder.loadMoreHistory(state: ShowingMatchHistoryState): ViewState {
        viewHolderScope.launch {
            try {
                val data = matchHistoryInteractor.getMatchData(state.puuid, state.region, state.matches.size, CHUNK_SIZE)

                postIntent(
                    ShowHistoryDataIntent(
                        items = (state.matches + data.matches.map { matchDataMapper.map(it) }).toPersistentList(),
                        hasMoreToLoad = data.hasMoreToLoad,
                    )
                )
            } catch(ex: CancellationException) {
                throw ex
            } catch(ex: Exception) {
                logError("Error loading more matches", ex)
                // TODO: Implement state with faild-and-reload bottom item
            }
        }

        return state.copy(
            isLoading = true,
        )
    }

    private fun showHistoryData(puuid: String, region: Region, lastLoadedAt: Long, items: ImmutableList<ItemToShow>, hasMoreToLoad: Boolean): ViewState {
        return ShowingMatchHistoryState(
            puuid = puuid,
            region = region,
            lastLoadedAt = lastLoadedAt,
            matches = items,
            isLoading = false,
            canLoadMore = hasMoreToLoad,
        )
    }

    private companion object {
        const val CHUNK_SIZE = 10
    }
}
