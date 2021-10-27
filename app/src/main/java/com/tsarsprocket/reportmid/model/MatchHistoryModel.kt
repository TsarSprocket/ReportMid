package com.tsarsprocket.reportmid.model

import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.tsarsprocket.reportmid.di.assisted.MatchModelFactory
import com.tsarsprocket.reportmid.summoner.model.SummonerModel
import com.tsarsprocket.reportmid.riotapi.matchV4.MatchV4Service
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class MatchHistoryModel @AssistedInject constructor(
    @Assisted val region: RegionModel,
    @Assisted val summoner: SummonerModel,
    val repository: Repository,
    private val matchModelFactory: MatchModelFactory,
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
                .map { matchList ->
                    LoadResult.Page(
                        matchList.matches.map { matchReferenceDto ->
                            MyMatch(summoner,
                                matchReferenceDto.gameId,
                                matchV4Service.match(matchReferenceDto.gameId).firstOrError().map { matchDto -> matchModelFactory.create( matchDto, region ) }
                                    .subscribeOn(Schedulers.io()).retry(3).cache()
                            ) },
                        prevKey = if (gameNo > 0) if (gameNo - params.loadSize < 0) 0 else gameNo - params.loadSize else null,
                        nextKey = if(matchList.matches.isNotEmpty()) gameNo + matchList.matches.size else null
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
        val gameId: Long,
        val match: Single<MatchModel>,
    )
}