package com.tsarsprocket.reportmid.model

import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.tsarsprocket.reportmid.di.assisted.MatchModelFactory
import com.tsarsprocket.reportmid.lol.model.Region
import com.tsarsprocket.reportmid.summoner.model.SummonerModel
import com.tsarsprocket.reportmid.riotapi.matchV5.MatchV5Service
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class MatchHistoryModel @AssistedInject constructor(
    @Assisted val region: Region,
    @Assisted val summoner: SummonerModel,
    val repository: Repository,
    private val matchModelFactory: MatchModelFactory,
): RxPagingSource<Int, MatchHistoryModel.MyMatch>() {

    private val matchV5Service = repository.retrofitServiceProvider.getService(region, MatchV5Service::class.java)
    private var lastGameId: Long? = null

    override fun getRefreshKey(state: PagingState<Int, MyMatch>): Int? = state.anchorPosition?.let { anchorPos ->
        state.closestPageToPosition(anchorPos)?.let { anchorPage ->
            val pageSize = state.config.pageSize
            anchorPage.prevKey?.plus(pageSize) ?: anchorPage.nextKey?.minus(pageSize)
        }
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, MyMatch>> =
        (params.key ?: 0).let{ gameNo ->
            matchV5Service.matches(summoner.puuid, start = gameNo, count = params.loadSize)
                .subscribeOn(Schedulers.io())
                .firstOrError()
                .map { matchIdList ->
                    LoadResult.Page(
                        matchIdList.map { matchId ->
                            MyMatch(summoner, matchId, matchV5Service.match(matchId).firstOrError().map { matchDto -> matchModelFactory.create( matchDto, region ) }
                                    .subscribeOn(Schedulers.io()).retry(3).cache())
                        },
                        prevKey = if (gameNo > 0) if (gameNo - params.loadSize < 0) 0 else gameNo - params.loadSize else null,
                        nextKey = if(matchIdList.isNotEmpty()) gameNo + matchIdList.size else null
                    ) as LoadResult<Int,MyMatch>
                }
                .onErrorReturn {
                    LoadResult.Error(it)
                }
        }

    data class MyMatch(
        val summoner: SummonerModel,
        val matchId: String,
        val match: Single<MatchModel>,
    )
}