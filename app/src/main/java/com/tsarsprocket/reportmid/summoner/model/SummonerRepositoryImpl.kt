package com.tsarsprocket.reportmid.summoner.model

import com.tsarsprocket.reportmid.di.qualifiers.IoScheduler
import com.tsarsprocket.reportmid.model.PuuidAndRegion
import com.tsarsprocket.reportmid.model.RegionModel
import com.tsarsprocket.reportmid.model.Repository
import com.tsarsprocket.reportmid.request_manager.model.Request
import com.tsarsprocket.reportmid.request_manager.model.RequestKey
import com.tsarsprocket.reportmid.request_manager.model.RequestManager
import com.tsarsprocket.reportmid.request_manager.model.RequestResult
import com.tsarsprocket.reportmid.riotapi.RetrofitServiceProvider
import com.tsarsprocket.reportmid.riotapi.summoner.Summoner
import com.tsarsprocket.reportmid.riotapi.summoner.SummonerV4Service
import com.tsarsprocket.reportmid.room.MainStorage
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class SummonerRepositoryImpl @Inject constructor(
    private val retrofitServiceProvider: RetrofitServiceProvider,
    private val requestManager: RequestManager,
    private val database: MainStorage,
    @IoScheduler private val ioScheduler: Scheduler,
    repositoryProvider: Provider<Repository>,
) : SummonerRepository {

    private val repository: Repository by lazy { repositoryProvider.get() }
    private val summonerCacheByAccountId = ConcurrentHashMap<AccountIdKey, SummonerModel>()
    private val summonerCacheByPuuid = ConcurrentHashMap<PuuidAndRegion, SummonerModel>()
    private val summonerCacheBySummonerId = ConcurrentHashMap<SummonerIdKey, SummonerModel>()
    private val summonerCacheBySummonerName = ConcurrentHashMap<SummonerNameKey, SummonerModel>()

    override fun getByAccountId(accountId: String, region: RegionModel): Single<SummonerModel> {
        val key = AccountIdKey( accountId, region )
        return summonerCacheByAccountId[ key ]?.let { summonerModel -> Single.just( summonerModel ) } ?:
            fetchSummonerByAccountId( key )
                .subscribeOn( ioScheduler )
                .doOnSuccess { summonerModel ->
                    summonerCacheByAccountId[ key ] = summonerModel
                    summonerCacheByPuuid[ PuuidAndRegion( summonerModel.puuid, summonerModel.region ) ] = summonerModel
                    summonerCacheBySummonerId[ SummonerIdKey( summonerModel.id, summonerModel.region ) ] = summonerModel
                    summonerCacheBySummonerName[ SummonerNameKey( summonerModel.name, summonerModel.region ) ] = summonerModel
                }
    }

    override fun getByPuuidAndRegion( puuidAndRegion: PuuidAndRegion ): Single<SummonerModel> {
        return summonerCacheByPuuid[ puuidAndRegion ]?.let { summonerModel -> Single.just( summonerModel ) } ?:
            fetchSummonerByPuuid( puuidAndRegion )
                .subscribeOn( ioScheduler )
                .doOnSuccess { summonerModel ->
                    summonerCacheByAccountId[ AccountIdKey( summonerModel.riotAccountId, summonerModel.region ) ] = summonerModel
                    summonerCacheByPuuid[ puuidAndRegion ] = summonerModel
                    summonerCacheBySummonerId[ SummonerIdKey( summonerModel.id, summonerModel.region ) ] = summonerModel
                    summonerCacheBySummonerName[ SummonerNameKey( summonerModel.name, summonerModel.region ) ] = summonerModel
                }
    }

    override fun getBySummonerId(summonerId: String, region: RegionModel): Single<SummonerModel> {
        val key = SummonerIdKey( summonerId, region )
        return summonerCacheBySummonerId[ key ]?.let { summonerModel -> Single.just( summonerModel ) } ?:
            fetchSummonerBySummonerId( key )
                .subscribeOn( ioScheduler )
                .doOnSuccess { summonerModel ->
                    summonerCacheByAccountId[ AccountIdKey( summonerModel.riotAccountId, summonerModel.region ) ] = summonerModel
                    summonerCacheByPuuid[ PuuidAndRegion( summonerModel.puuid, summonerModel.region ) ] = summonerModel
                    summonerCacheBySummonerId[ key ] = summonerModel
                    summonerCacheBySummonerName[ SummonerNameKey( summonerModel.name, summonerModel.region ) ] = summonerModel
                }
    }

    override fun getBySummonerName(summonerName: String, region: RegionModel): Single<SummonerModel> {
        val key = SummonerNameKey( summonerName, region )
        return summonerCacheBySummonerName[ key ]?.let { summonerModel -> Single.just( summonerModel ) } ?:
            fetchSummonerBySummonerName( key )
                .subscribeOn( ioScheduler )
                .doOnSuccess { summonerModel ->
                    summonerCacheByAccountId[ AccountIdKey( summonerModel.riotAccountId, summonerModel.region ) ] = summonerModel
                    summonerCacheByPuuid[ PuuidAndRegion( summonerModel.puuid, summonerModel.region ) ] = summonerModel
                    summonerCacheBySummonerId[ SummonerIdKey( summonerModel.id, summonerModel.region ) ] = summonerModel
                    summonerCacheBySummonerName[ key ] = summonerModel
                }
    }

    override fun getMine(): Observable<List<SummonerModel>> {
        return database.summonerDAO().getMySummoners()
            .switchMap { lst ->
                Observable.combineLatest(
                lst.map { sumEnt ->
                    val reg = database.regionDAO().getById(sumEnt.regionId)
                    getByPuuidAndRegion(PuuidAndRegion(sumEnt.puuid, RegionModel.getByTag(reg.tag))).toObservable()
                }, fun(arr: Array<Any>): List<SummonerModel> = arr.asList().map { it as SummonerModel } )
            }
    }

    override fun getMineForRegionSelected(reg: RegionModel): Observable<List<Pair<SummonerModel,Boolean>>> {
        return database.regionDAO().getByTagObservable(reg.tag)
            .switchMap { regEnt ->
                database.currentAccountDAO().getByRegionIdObservable(regEnt.id)
                    .map { lstCurAcc ->
                        val firstOne = lstCurAcc.first()
                        Pair(regEnt, database.myAccountDAO().getById(firstOne.accountId))
                    }
            }
            .switchMap { (regEnt, myCurAccEnt) ->
                database.summonerDAO().getMySummonersByRegionObservable(regEnt.id)
                    .map { sumEntities -> sumEntities.map { sumEnt -> Pair(sumEnt, sumEnt.id == myCurAccEnt.summonerId) } }
                    .map { lst ->
                        lst.map { (sumEnt, isSelected) ->
                            getByPuuidAndRegion(PuuidAndRegion(sumEnt.puuid,RegionModel.getByTag(regEnt.tag)))
                                .map { sum -> Pair(sum, isSelected) }.blockingGet()
                        }
                    }
            }
    }

    private fun fetchSummonerByAccountId( key: AccountIdKey ): Single<SummonerModel> =
        requestManager.addRequest( SummonerRequestByAccountId( key ) )
            .map { requestResult -> SummonerModel( repository, requestResult.summoner, key.region ) }

    private fun fetchSummonerByPuuid(puuidAndRegion: PuuidAndRegion): Single<SummonerModel> =
        requestManager.addRequest( SummonerRequestByPuuid( PuuidKey( puuidAndRegion ) ) )
            .map { requestResult -> SummonerModel( repository, requestResult.summoner, puuidAndRegion.region ) }

    private fun fetchSummonerBySummonerId( key: SummonerIdKey ): Single<SummonerModel> =
        requestManager.addRequest( SummonerRequestBySummonerId( key ) )
            .map { requestResult -> SummonerModel( repository, requestResult.summoner, key.region ) }

    private fun fetchSummonerBySummonerName( key: SummonerNameKey ): Single<SummonerModel> =
        requestManager.addRequest( SummonerRequestBySummonerName( key ) )
            .map { requestResult -> SummonerModel( repository, requestResult.summoner, key.region ) }

    // Classes

    private data class AccountIdKey(
        val accountId: String,
        val region: RegionModel,
    ): RequestKey

    private data class PuuidKey(
        val puuidAndRegion: PuuidAndRegion,
    ) : RequestKey

    private data class SummonerNameKey(
        val summonerName: String,
        val region: RegionModel,
    ) : RequestKey

    private data class SummonerIdKey(
        val summonerId: String,
        val region: RegionModel,
    ) : RequestKey

    private inner class SummonerRequestByAccountId( key: AccountIdKey ) :
            Request<AccountIdKey, SummonerRequestResult>( key ) {
        override fun invoke(): SummonerRequestResult {
            return retrofitServiceProvider.getService( key.region, SummonerV4Service::class.java ).getByAccountId( key.accountId )
                .map { summoner -> SummonerRequestResult( summoner ) }
                .blockingGet()
        }
    }

    private inner class SummonerRequestByPuuid( key: PuuidKey ) :
            Request<PuuidKey,SummonerRequestResult>( key ) {
        override fun invoke(): SummonerRequestResult {
            return retrofitServiceProvider.getService( key.puuidAndRegion.region, SummonerV4Service::class.java ).getByPuuid( key.puuidAndRegion.puuid )
                .map { summoner -> SummonerRequestResult( summoner ) }
                .blockingGet()
        }
    }

    private inner class SummonerRequestBySummonerId( key: SummonerIdKey ) :
            Request<SummonerIdKey,SummonerRequestResult>( key ) {
        override fun invoke(): SummonerRequestResult {
            return retrofitServiceProvider.getService( key.region, SummonerV4Service::class.java ).getBySummonerId( key.summonerId )
                .map { summoner -> SummonerRequestResult( summoner ) }
                .blockingGet()
        }
    }

    private inner class SummonerRequestBySummonerName( key: SummonerNameKey ) :
            Request<SummonerNameKey,SummonerRequestResult>( key ) {
        override fun invoke(): SummonerRequestResult {
            return retrofitServiceProvider.getService( key.region, SummonerV4Service::class.java ).getBySummonerName( key.summonerName )
                .map { summoner -> SummonerRequestResult( summoner ) }
                .blockingGet()
        }
    }

    private data class SummonerRequestResult(
        val summoner: Summoner,
    ) : RequestResult
}