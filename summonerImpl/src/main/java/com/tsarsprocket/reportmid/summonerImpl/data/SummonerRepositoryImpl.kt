package com.tsarsprocket.reportmid.summonerImpl.data

import com.tsarsprocket.reportmid.appApi.room.MainStorage
import com.tsarsprocket.reportmid.baseApi.data.NoDataFoundException
import com.tsarsprocket.reportmid.baseApi.di.qualifiers.Computation
import com.tsarsprocket.reportmid.baseApi.di.qualifiers.Io
import com.tsarsprocket.reportmid.lol.model.Puuid
import com.tsarsprocket.reportmid.lol.model.PuuidAndRegion
import com.tsarsprocket.reportmid.lol.model.Region
import com.tsarsprocket.reportmid.lolServicesApi.riotapi.ServiceFactory
import com.tsarsprocket.reportmid.lolServicesApi.riotapi.getService
import com.tsarsprocket.reportmid.requestManagerApi.data.RequestManager
import com.tsarsprocket.reportmid.requestManagerApi.data.request
import com.tsarsprocket.reportmid.summonerApi.data.SummonerRepository
import com.tsarsprocket.reportmid.summonerApi.model.ChampionMastery
import com.tsarsprocket.reportmid.summonerApi.model.MyAccount
import com.tsarsprocket.reportmid.summonerApi.model.Summoner
import com.tsarsprocket.reportmid.summonerApi.model.SummonerInfo
import com.tsarsprocket.reportmid.summonerImpl.model.MyAccountImpl
import com.tsarsprocket.reportmid.summonerImpl.model.SummonerInfoImpl
import com.tsarsprocket.reportmid.summonerImpl.retrofit.championMastery.ChampionMasteryDto
import com.tsarsprocket.reportmid.summonerImpl.retrofit.championMastery.ChampionMasteryV4
import com.tsarsprocket.reportmid.summonerImpl.retrofit.summoner.SummonerDto
import com.tsarsprocket.reportmid.summonerImpl.retrofit.summoner.SummonerV4Service
import com.tsarsprocket.reportmid.summonerRoom.MyAccountEntity
import com.tsarsprocket.reportmid.summonerRoom.SummonerEntity
import com.tsarsprocket.reportmid.utils.data.ExpiringValue
import com.tsarsprocket.reportmid.utils.data.expiring
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class SummonerRepositoryImpl @Inject constructor(
    private val serviceFactory: ServiceFactory,
    private val requestManager: RequestManager,
    private val storage: MainStorage,
    @Io private val ioDispatcher: CoroutineDispatcher,
    @Computation private val computationDispatcher: CoroutineDispatcher,
) : SummonerRepository {

    private val summonerCacheByPuuid = ConcurrentHashMap<PuuidAndRegion, ExpiringValue<Summoner>>()
    private val summonerCacheBySummonerId = ConcurrentHashMap<SummonerIdKey, ExpiringValue<Summoner>>()
    private val summonerCacheBySummonerName = ConcurrentHashMap<SummonerNameKey, ExpiringValue<Summoner>>()

    override suspend fun addKnownSummoner(puuid: Puuid, region: Region): SummonerInfo = withContext(ioDispatcher) {
        SummonerInfoImpl(
            id = storage.summonerDAO().insert(SummonerEntity(puuid = puuid.value, regionId = region.id)),
            puuid = puuid,
            region = region,
        )
    }

    override suspend fun createMyAccount(summonerId: Long): MyAccount = withContext(ioDispatcher) {
        val entity = MyAccountEntity(summonerId)
        entity.id = storage.myAccountDAO().insert(entity)
        MyAccountImpl(entity)
    }

    override suspend fun deleteMyAccount(myAccount: MyAccount) {
        storage.myAccountDAO().getById(myAccount.id)?.let { storage.myAccountDAO().delete(it) } ?: throw NoDataFoundException()
    }

    override suspend fun forgetSummonerById(id: Long) = withContext(ioDispatcher) {
        storage.summonerDAO().getById(id)?.let { storage.summonerDAO().delete(it) } ?: throw NoDataFoundException()
    }

    override suspend fun getAllMyAccounts(): List<MyAccount> = withContext(ioDispatcher) {
        storage.myAccountDAO().getAll().map { entity -> MyAccountImpl(entity) }
    }

    override suspend fun getKnownSummonerId(puuidAndRegion: PuuidAndRegion): Long = withContext(ioDispatcher) {
        storage.summonerDAO().getByPuuidAndRegionId(puuidAndRegion.puuid.value, puuidAndRegion.region.id)?.id ?: throw NoDataFoundException()
    }

    override suspend fun getMyAccountById(id: Long): MyAccount = withContext(ioDispatcher) {
        storage.myAccountDAO().getById(id)?.let { entity ->
            MyAccountImpl(id, entity.summonerId)
        } ?: throw NoDataFoundException()
    }

    override suspend fun getMyAccountByPuuidAndRegion(puuidAndRegion: PuuidAndRegion): MyAccount = withContext(ioDispatcher) {
        storage.myAccountDAO().getByPuuidAndRegionId(puuidAndRegion.puuid.value, puuidAndRegion.region.id)?.run {
            MyAccountImpl(id = id, summonerId = summonerId)
        } ?: throw NoDataFoundException()
    }

    override suspend fun getMyAccountsByRegion(region: Region): List<MyAccount> = withContext(ioDispatcher) {
        storage.myAccountDAO().getByRegion(region.id).map { MyAccountImpl(id = it.id, summonerId = it.summonerId) }
    }

    override suspend fun getMyAccountBySummonerId(summonerId: Long): MyAccount = withContext(ioDispatcher) {
        storage.myAccountDAO().getBySummonerId(summonerId)?.run {
            MyAccountImpl(id, summonerId)
        } ?: throw NoDataFoundException()
    }

    override suspend fun getNumberOfMyAccounts(): Int = withContext(ioDispatcher) {
        storage.myAccountDAO().count()
    }

    override suspend fun getSummonerInfoById(id: Long): SummonerInfo = withContext(ioDispatcher) {
        storage.summonerDAO().getById(id)?.let { SummonerInfoImpl(it) } ?: throw NoDataFoundException()
    }

    override suspend fun isSummonerKnown(puuidAndRegion: PuuidAndRegion): Boolean = withContext(ioDispatcher) {
        storage.summonerDAO().getByPuuidAndRegionId(puuid = puuidAndRegion.puuid.value, regionId = puuidAndRegion.region.id) != null
    }

    override suspend fun requestRemoteSummonerByPuuidAndRegion(puuidAndRegion: PuuidAndRegion): Summoner {
        return summonerCacheByPuuid[puuidAndRegion]?.getIfValid(TTL) ?: withContext(computationDispatcher) { fetchSummonerByPuuid(puuidAndRegion) }.also { summoner ->
            val expiring = summoner.expiring
            summonerCacheByPuuid[puuidAndRegion] = expiring
            summonerCacheBySummonerId[SummonerIdKey(summoner.riotId, summoner.region)] = expiring
            summonerCacheBySummonerName[SummonerNameKey(summoner.name, summoner.region)] = expiring
        }
    }

    override suspend fun requestRemoteSummonerByRiotId(id: String, region: Region): Summoner {
        val key = SummonerIdKey(id, region)
        return summonerCacheBySummonerId[key]?.getIfValid(TTL) ?: withContext(computationDispatcher) {
            fetchSummonerBySummonerId(key).also { summoner ->
                val expiring = summoner.expiring
                summonerCacheByPuuid[PuuidAndRegion(summoner.puuid, summoner.region)] = expiring
                summonerCacheBySummonerId[key] = expiring
                summonerCacheBySummonerName[SummonerNameKey(summoner.name, summoner.region)] = expiring
            }
        }
    }

    override suspend fun requestRemoteSummonerByName(name: String, region: Region): Summoner {
        val key = SummonerNameKey(name, region)
        return summonerCacheBySummonerName[key]?.getIfValid(TTL) ?: withContext(computationDispatcher) {
            fetchSummonerBySummonerName(key).also { summoner ->
                val expiring = summoner.expiring
                summonerCacheByPuuid[PuuidAndRegion(summoner.puuid, summoner.region)] = expiring
                summonerCacheBySummonerId[SummonerIdKey(summoner.riotId, summoner.region)] = expiring
                summonerCacheBySummonerName[key] = expiring
            }
        }
    }

    override suspend fun getMySummoners(): List<Summoner> = withContext(computationDispatcher) {
        withContext(ioDispatcher) { storage.summonerDAO().getMySummoners() }
            .map { summonerEntity ->
                val reg = Region.getById(summonerEntity.regionId)
                requestRemoteSummonerByPuuidAndRegion(
                    PuuidAndRegion(
                        Puuid(summonerEntity.puuid),
                        Region.getByTag(reg.tag)
                    )
                )
            }
    }

    override suspend fun getMySummonersForRegion(reg: Region): List<Summoner> = withContext(ioDispatcher) {
        coroutineScope {
            storage.summonerDAO().getMySummonersByRegion(reg.id).map {
                async { requestRemoteSummonerByPuuidAndRegion(PuuidAndRegion(Puuid(it.puuid), Region.getById(it.regionId))) }
            }.awaitAll()
        }
    }

    override suspend fun requestSummonerByMyAccount(myAccount: MyAccount): Summoner = withContext(ioDispatcher) {
        storage.summonerDAO().getById(myAccount.summonerId)?.run {
            fetchSummonerByPuuid(PuuidAndRegion(Puuid(puuid), Region.getById(regionId)))
        } ?: throw NoDataFoundException()
    }

    private suspend fun fetchChampionMasteriesByPuuid(key: MasteriesPuuidKey): List<ChampionMastery> {
        val championMasteriesRequestResult: ChampionMasteriesRequestResult = requestManager.request(ChampionMasteriesByPuuid(key))
        return championMasteriesRequestResult.championMasteryDtos.map { dto ->
            ChampionMastery(
                championId = dto.championId,
                level = dto.championLevel,
                points = dto.championPoints,
            )
        }
    }

    private suspend fun fetchSummonerByAccountId(key: AccountIdKey): Summoner =
        requestManager.request(SummonerRequestByAccountId(key))
            .let { requestResult ->
                with(requestResult.summonerDto) {
                    Summoner(
                        region = key.region,
                        riotId = riotId,
                        name = name,
                        iconId = profileIconId,
                        puuid = Puuid(puuid),
                        level = summonerLevel,
                        masteriesProvider = { fetchChampionMasteriesByPuuid(MasteriesPuuidKey(Puuid(requestResult.summonerDto.puuid), key.region)) },
                    )
                }
            }

    private suspend fun fetchSummonerByPuuid(puuidAndRegion: PuuidAndRegion): Summoner {
        return with(requestManager.request(SummonerRequestByPuuid(PuuidKey(puuidAndRegion))).summonerDto) {
            Summoner(
                region = puuidAndRegion.region,
                riotId = riotId,
                name = name,
                iconId = profileIconId,
                puuid = Puuid(puuid),
                level = summonerLevel,
                masteriesProvider = { fetchChampionMasteriesByPuuid(MasteriesPuuidKey(Puuid(puuid), puuidAndRegion.region)) },
            )
        }
    }

    private suspend fun fetchSummonerBySummonerId(key: SummonerIdKey): Summoner {
        return with(requestManager.request(SummonerRequestBySummonerId(key)).summonerDto) {
            Summoner(
                region = key.region,
                riotId = riotId,
                name = name,
                iconId = profileIconId,
                puuid = Puuid(puuid),
                level = summonerLevel,
                masteriesProvider = { fetchChampionMasteriesByPuuid(MasteriesPuuidKey(Puuid(puuid), key.region)) },
            )
        }
    }

    private suspend fun fetchSummonerBySummonerName(key: SummonerNameKey): Summoner {
        val result = requestManager.request(SummonerRequestBySummonerName(key))
        return with(result.summonerDto) {
            Summoner(
                region = key.region,
                riotId = riotId,
                name = name,
                iconId = profileIconId,
                puuid = Puuid(puuid),
                level = summonerLevel,
                masteriesProvider = { fetchChampionMasteriesByPuuid(MasteriesPuuidKey(Puuid(puuid), key.region)) },
            )
        }
    }

    // Classes

    private data class AccountIdKey(
        val accountId: String,
        val region: Region,
    ) : com.tsarsprocket.reportmid.requestManagerApi.data.RequestKey

    private data class PuuidKey(
        val puuidAndRegion: PuuidAndRegion,
    ) : com.tsarsprocket.reportmid.requestManagerApi.data.RequestKey

    private data class MasteriesPuuidKey(
        val puuid: Puuid,
        val region: Region,
    ) : com.tsarsprocket.reportmid.requestManagerApi.data.RequestKey

    private data class SummonerNameKey(
        val summonerName: String,
        val region: Region,
    ) : com.tsarsprocket.reportmid.requestManagerApi.data.RequestKey

    private data class SummonerIdKey(
        val summonerId: String,
        val region: Region,
    ) : com.tsarsprocket.reportmid.requestManagerApi.data.RequestKey

    private inner class ChampionMasteriesByPuuid(key: MasteriesPuuidKey) :
        com.tsarsprocket.reportmid.requestManagerApi.data.Request<MasteriesPuuidKey, ChampionMasteriesRequestResult>(key) {

        override suspend fun invoke(): ChampionMasteriesRequestResult {
            val championMasteryV4 = serviceFactory.getService<ChampionMasteryV4>(key.region)
            return ChampionMasteriesRequestResult(championMasteryV4.getByPuuid(key.puuid.value))
        }
    }

    private data class ChampionMasteriesRequestResult(
        val championMasteryDtos: List<ChampionMasteryDto>
    ) : com.tsarsprocket.reportmid.requestManagerApi.data.RequestResult

    private inner class SummonerRequestByAccountId(key: AccountIdKey) :
        com.tsarsprocket.reportmid.requestManagerApi.data.Request<AccountIdKey, SummonerRequestResult>(key) {

        override suspend fun invoke(): SummonerRequestResult {
            val summonerV4Service = serviceFactory.getService<SummonerV4Service>(key.region)
            return SummonerRequestResult(summonerV4Service.getByAccountId(key.accountId))
        }
    }

    private inner class SummonerRequestByPuuid(key: PuuidKey) : com.tsarsprocket.reportmid.requestManagerApi.data.Request<PuuidKey, SummonerRequestResult>(key) {

        override suspend fun invoke(): SummonerRequestResult {
            val summonerV4Service = serviceFactory.getService<SummonerV4Service>(key.puuidAndRegion.region)
            return SummonerRequestResult(summonerV4Service.getByPuuid(key.puuidAndRegion.puuid.value))
        }
    }

    private inner class SummonerRequestBySummonerId(key: SummonerIdKey) :
        com.tsarsprocket.reportmid.requestManagerApi.data.Request<SummonerIdKey, SummonerRequestResult>(key) {

        override suspend fun invoke(): SummonerRequestResult {
            val summonerV4Service = serviceFactory.getService<SummonerV4Service>(key.region)
            return SummonerRequestResult(summonerV4Service.getBySummonerId(key.summonerId))
        }
    }

    private inner class SummonerRequestBySummonerName(key: SummonerNameKey) :
        com.tsarsprocket.reportmid.requestManagerApi.data.Request<SummonerNameKey, SummonerRequestResult>(key) {

        override suspend fun invoke(): SummonerRequestResult {
            val summonerV4Service = serviceFactory.getService<SummonerV4Service>(key.region)
            return SummonerRequestResult(summonerV4Service.getBySummonerName(key.summonerName))
        }
    }

    private data class SummonerRequestResult(
        val summonerDto: SummonerDto,
    ) : com.tsarsprocket.reportmid.requestManagerApi.data.RequestResult

    companion object {

        /**
         * Time to live in millis
         */
        const val TTL = 1L * 1000 * 60
    }
}