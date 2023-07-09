package com.tsarsprocket.reportmid.summoner.model

import com.tsarsprocket.reportmid.base.di.AppScope
import com.tsarsprocket.reportmid.di.qualifiers.IoScheduler
import com.tsarsprocket.reportmid.lol.model.PuuidAndRegion
import com.tsarsprocket.reportmid.lol.model.Region
import com.tsarsprocket.reportmid.lol_services_api.riotapi.ServiceFactory
import com.tsarsprocket.reportmid.lol_services_api.riotapi.getService
import com.tsarsprocket.reportmid.request_manager.model.Request
import com.tsarsprocket.reportmid.request_manager.model.RequestKey
import com.tsarsprocket.reportmid.request_manager.model.RequestManager
import com.tsarsprocket.reportmid.request_manager.model.RequestResult
import com.tsarsprocket.reportmid.riotapi.championMastery.ChampionMasteryDto
import com.tsarsprocket.reportmid.riotapi.championMastery.ChampionMasteryV4
import com.tsarsprocket.reportmid.riotapi.summoner.SummonerDto
import com.tsarsprocket.reportmid.riotapi.summoner.SummonerV4Service
import com.tsarsprocket.reportmid.room.MainStorage
import com.tsarsprocket.reportmid.summoner.di.ChampionMasteryModelFactory
import com.tsarsprocket.reportmid.summoner.di.SummonerModelFactory
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

@AppScope
class SummonerRepositoryImpl @Inject constructor(
    private val championMasteryModelFactory: ChampionMasteryModelFactory,
    private val summonerModelFactory: SummonerModelFactory,
    private val serviceFactory: ServiceFactory,
    private val requestManager: RequestManager,
    private val database: MainStorage,
    @IoScheduler private val ioScheduler: Scheduler,
) : SummonerRepository {

    private val summonerCacheByAccountId = ConcurrentHashMap<AccountIdKey, SummonerModel>()
    private val summonerCacheByPuuid = ConcurrentHashMap<PuuidAndRegion, SummonerModel>()
    private val summonerCacheBySummonerId = ConcurrentHashMap<SummonerIdKey, SummonerModel>()
    private val summonerCacheBySummonerName = ConcurrentHashMap<SummonerNameKey, SummonerModel>()

    override fun getByAccountId(accountId: String, region: Region): Single<SummonerModel> {
        val key = AccountIdKey(accountId, region)
        return summonerCacheByAccountId[key]?.let { summonerModel -> Single.just(summonerModel) } ?: fetchSummonerByAccountId(key)
            .subscribeOn(ioScheduler)
            .doOnSuccess { summonerModel ->
                summonerCacheByAccountId[key] = summonerModel
                summonerCacheByPuuid[PuuidAndRegion(summonerModel.puuid, summonerModel.region)] = summonerModel
                summonerCacheBySummonerId[SummonerIdKey(summonerModel.id, summonerModel.region)] = summonerModel
                summonerCacheBySummonerName[SummonerNameKey(summonerModel.name, summonerModel.region)] = summonerModel
            }
    }

    override fun getByPuuidAndRegion(puuidAndRegion: PuuidAndRegion): Single<SummonerModel> {
        return summonerCacheByPuuid[puuidAndRegion]?.let { summonerModel -> Single.just(summonerModel) } ?: fetchSummonerByPuuid(puuidAndRegion)
            .subscribeOn(ioScheduler)
            .doOnSuccess { summonerModel ->
                summonerCacheByAccountId[AccountIdKey(summonerModel.riotAccountId, summonerModel.region)] = summonerModel
                summonerCacheByPuuid[puuidAndRegion] = summonerModel
                summonerCacheBySummonerId[SummonerIdKey(summonerModel.id, summonerModel.region)] = summonerModel
                summonerCacheBySummonerName[SummonerNameKey(summonerModel.name, summonerModel.region)] = summonerModel
            }
    }

    override fun getBySummonerId(summonerId: String, region: Region): Single<SummonerModel> {
        val key = SummonerIdKey(summonerId, region)
        return summonerCacheBySummonerId[key]?.let { summonerModel -> Single.just(summonerModel) } ?: fetchSummonerBySummonerId(key)
            .subscribeOn(ioScheduler)
            .doOnSuccess { summonerModel ->
                summonerCacheByAccountId[AccountIdKey(summonerModel.riotAccountId, summonerModel.region)] = summonerModel
                summonerCacheByPuuid[PuuidAndRegion(summonerModel.puuid, summonerModel.region)] = summonerModel
                summonerCacheBySummonerId[key] = summonerModel
                summonerCacheBySummonerName[SummonerNameKey(summonerModel.name, summonerModel.region)] = summonerModel
            }
    }

    override fun getBySummonerName(summonerName: String, region: Region): Single<SummonerModel> {
        val key = SummonerNameKey(summonerName, region)
        return summonerCacheBySummonerName[key]?.let { summonerModel -> Single.just(summonerModel) } ?: fetchSummonerBySummonerName(key)
            .subscribeOn(ioScheduler)
            .doOnSuccess { summonerModel ->
                summonerCacheByAccountId[AccountIdKey(summonerModel.riotAccountId, summonerModel.region)] = summonerModel
                summonerCacheByPuuid[PuuidAndRegion(summonerModel.puuid, summonerModel.region)] = summonerModel
                summonerCacheBySummonerId[SummonerIdKey(summonerModel.id, summonerModel.region)] = summonerModel
                summonerCacheBySummonerName[key] = summonerModel
            }
    }

    override fun getMine(): Observable<List<SummonerModel>> {
        return database.summonerDAO().getMySummoners()
            .switchMap { lst ->
                Observable.combineLatest(
                    lst.map { sumEnt ->
                        val reg = database.regionDAO().getById(sumEnt.regionId)
                        getByPuuidAndRegion(PuuidAndRegion(sumEnt.puuid, Region.getByTag(reg.tag))).toObservable()
                    }, fun(arr: Array<Any>): List<SummonerModel> = arr.asList().map { it as SummonerModel })
            }
    }

    override fun getMineForRegionSelected(reg: Region): Observable<List<Pair<SummonerModel, Boolean>>> {
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
                            getByPuuidAndRegion(PuuidAndRegion(sumEnt.puuid, Region.getByTag(regEnt.tag)))
                                .map { sum -> Pair(sum, isSelected) }.blockingGet()
                        }
                    }
            }
    }

    private fun fetchChampionMasteriesBySummonerId(key: SummonerIdKey): Single<List<ChampionMasteryModel>> =
        requestManager.addRequest(ChampionMasteriesBySummonerId(key))
            .map { requestResult -> requestResult.championMasteryDtos.map { championMasteryModelFactory.create(it) } }

    private fun fetchSummonerByAccountId(key: AccountIdKey): Single<SummonerModel> =
        requestManager.addRequest(SummonerRequestByAccountId(key))
            .map { requestResult ->
                summonerModelFactory.create(
                    key.region, requestResult.summonerDto,
                    fetchChampionMasteriesBySummonerId(SummonerIdKey(requestResult.summonerDto.id, key.region))
                )
            }

    private fun fetchSummonerByPuuid(puuidAndRegion: PuuidAndRegion): Single<SummonerModel> =
        requestManager.addRequest(SummonerRequestByPuuid(PuuidKey(puuidAndRegion)))
            .map { requestResult ->
                summonerModelFactory.create(
                    puuidAndRegion.region, requestResult.summonerDto,
                    fetchChampionMasteriesBySummonerId(SummonerIdKey(requestResult.summonerDto.id, puuidAndRegion.region))
                )
            }

    private fun fetchSummonerBySummonerId(key: SummonerIdKey): Single<SummonerModel> =
        requestManager.addRequest(SummonerRequestBySummonerId(key))
            .map { requestResult ->
                summonerModelFactory.create(
                    key.region, requestResult.summonerDto,
                    fetchChampionMasteriesBySummonerId(SummonerIdKey(requestResult.summonerDto.id, key.region))
                )
            }

    private fun fetchSummonerBySummonerName(key: SummonerNameKey): Single<SummonerModel> =
        requestManager.addRequest(SummonerRequestBySummonerName(key))
            .map { requestResult ->
                summonerModelFactory.create(
                    key.region, requestResult.summonerDto,
                    fetchChampionMasteriesBySummonerId(SummonerIdKey(requestResult.summonerDto.id, key.region))
                )
            }

    // Classes

    private data class AccountIdKey(
        val accountId: String,
        val region: Region,
    ) : RequestKey

    private data class PuuidKey(
        val puuidAndRegion: PuuidAndRegion,
    ) : RequestKey

    private data class SummonerNameKey(
        val summonerName: String,
        val region: Region,
    ) : RequestKey

    private data class SummonerIdKey(
        val summonerId: String,
        val region: Region,
    ) : RequestKey

    private inner class ChampionMasteriesBySummonerId(key: SummonerIdKey) :
        Request<SummonerIdKey, ChampionMasteriesRequestResult>(key) {

        override fun invoke(): ChampionMasteriesRequestResult {
            return serviceFactory.getService<ChampionMasteryV4>(key.region).getBySummonerId(key.summonerId)
                .map { masteryList -> ChampionMasteriesRequestResult(masteryList) }
                .blockingGet()
        }
    }

    private data class ChampionMasteriesRequestResult(
        val championMasteryDtos: List<ChampionMasteryDto>
    ) : RequestResult

    private inner class SummonerRequestByAccountId(key: AccountIdKey) :
        Request<AccountIdKey, SummonerRequestResult>(key) {

        override fun invoke(): SummonerRequestResult {
            return serviceFactory.getService<SummonerV4Service>(key.region).getByAccountId(key.accountId)
                .map { summoner -> SummonerRequestResult(summoner) }
                .blockingGet()
        }
    }

    private inner class SummonerRequestByPuuid(key: PuuidKey) :
        Request<PuuidKey, SummonerRequestResult>(key) {

        override fun invoke(): SummonerRequestResult {
            return serviceFactory.getService<SummonerV4Service>(key.puuidAndRegion.region).getByPuuid(key.puuidAndRegion.puuid)
                .map { summoner -> SummonerRequestResult(summoner) }
                .blockingGet()
        }
    }

    private inner class SummonerRequestBySummonerId(key: SummonerIdKey) :
        Request<SummonerIdKey, SummonerRequestResult>(key) {

        override fun invoke(): SummonerRequestResult {
            return serviceFactory.getService<SummonerV4Service>(key.region).getBySummonerId(key.summonerId)
                .map { summoner -> SummonerRequestResult(summoner) }
                .blockingGet()
        }
    }

    private inner class SummonerRequestBySummonerName(key: SummonerNameKey) :
        Request<SummonerNameKey, SummonerRequestResult>(key) {

        override fun invoke(): SummonerRequestResult {
            return serviceFactory.getService<SummonerV4Service>(key.region).getBySummonerName(key.summonerName)
                .map { summoner -> SummonerRequestResult(summoner) }
                .blockingGet()
        }
    }

    private data class SummonerRequestResult(
        val summonerDto: SummonerDto,
    ) : RequestResult
}