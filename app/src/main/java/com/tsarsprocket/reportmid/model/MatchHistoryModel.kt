package com.tsarsprocket.reportmid.model

import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.tsarsprocket.reportmid.riotapi.matchV4.MatchV4Service
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MatchHistoryModel(
    val repository: Repository,
    val region: RegionModel,
    val summoner: SummonerModel,
): RxPagingSource<Int, MatchHistoryModel.MyMatch>() {

    private val matchV4Service = repository.retrofitServiceProvider.getService(region, MatchV4Service::class.java)
    private var lastGameId: Long? = null

    override fun getRefreshKey(state: PagingState<Int, MyMatch>): Int? = state.anchorPosition?.let { anchorPos ->
        state.closestPageToPosition(anchorPos)?.let { anchorPage ->
            val pageSize = state.config.pageSize
            anchorPage.prevKey?.plus(pageSize) ?: anchorPage.nextKey?.minus(pageSize)
        }
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, MyMatch>> =
        (params.key ?: 0).let{ gameNo ->
            matchV4Service.matchList(summoner.riotAccountId, beginIndex = gameNo, endIndex = gameNo + params.loadSize)
                .subscribeOn(Schedulers.io())
                .firstOrError()
                .flatMap { matchList ->
                    Observable.merge(matchList.matches.map { match -> matchV4Service.match(match.gameId).firstOrError().toObservable() }).toList()
                }
                .map { lstMatchDtos ->
                    LoadResult.Page(
                        lstMatchDtos.mapNotNull { matchDto -> MyMatch(summoner, MatchModel(repository, matchDto, region)) },
                        prevKey = if (gameNo > 0) if (gameNo - params.loadSize < 0) 0 else gameNo - params.loadSize else null,
                        nextKey = if( lstMatchDtos.size > 0) gameNo + lstMatchDtos.size else null
                    ) as LoadResult<Int,MyMatch>
                }
                .onErrorReturn { LoadResult.Error(it) }
        }

    fun getValidityState(): Single<Boolean> =
        matchV4Service.matchList(summoner.riotAccountId, beginIndex = 0, endIndex = 1)
            .subscribeOn(Schedulers.io())
            .firstOrError()
            .map { matchListDto ->
                val gameId = matchListDto.matches.first().gameId
                if (lastGameId ==  null) { lastGameId = gameId; true } else { lastGameId == gameId }
            }

    data class MyMatch(
        val summoner: SummonerModel,
        val match: MatchModel,
    )
}