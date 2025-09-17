package com.tsarsprocket.reportmid.summonerImpl.data

import com.tsarsprocket.reportmid.appApi.room.MainStorage
import com.tsarsprocket.reportmid.baseApi.data.NoDataFoundException
import com.tsarsprocket.reportmid.baseApi.di.qualifiers.Computation
import com.tsarsprocket.reportmid.baseApi.di.qualifiers.Io
import com.tsarsprocket.reportmid.lol.api.model.Puuid
import com.tsarsprocket.reportmid.lol.api.model.PuuidAndRegion
import com.tsarsprocket.reportmid.lol.api.model.Region
import com.tsarsprocket.reportmid.lolServicesApi.riotapi.ServiceFactory
import com.tsarsprocket.reportmid.lolServicesApi.riotapi.getService
import com.tsarsprocket.reportmid.requestManagerApi.data.Request
import com.tsarsprocket.reportmid.requestManagerApi.data.RequestKey
import com.tsarsprocket.reportmid.requestManagerApi.data.RequestManager
import com.tsarsprocket.reportmid.requestManagerApi.data.request
import com.tsarsprocket.reportmid.summonerApi.data.SummonerRepository
import com.tsarsprocket.reportmid.summonerApi.model.ChampionMastery
import com.tsarsprocket.reportmid.summonerApi.model.Friend
import com.tsarsprocket.reportmid.summonerApi.model.MyAccount
import com.tsarsprocket.reportmid.summonerApi.model.RiotAccount
import com.tsarsprocket.reportmid.summonerApi.model.Summoner
import com.tsarsprocket.reportmid.summonerApi.model.SummonerInfo
import com.tsarsprocket.reportmid.summonerImpl.model.MyAccountImpl
import com.tsarsprocket.reportmid.summonerImpl.model.SummonerInfoImpl
import com.tsarsprocket.reportmid.summonerImpl.retrofit.account.AccountV1Service
import com.tsarsprocket.reportmid.summonerImpl.retrofit.championMastery.ChampionMasteryDto
import com.tsarsprocket.reportmid.summonerImpl.retrofit.championMastery.ChampionMasteryV4
import com.tsarsprocket.reportmid.summonerImpl.retrofit.summoner.SummonerDto
import com.tsarsprocket.reportmid.summonerImpl.retrofit.summoner.SummonerV4Service
import com.tsarsprocket.reportmid.summonerRoom.FriendEntity
import com.tsarsprocket.reportmid.summonerRoom.MyAccountEntity
import com.tsarsprocket.reportmid.summonerRoom.SummonerEntity
import com.tsarsprocket.reportmid.utils.common.noWhitespaces
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

    override suspend fun createMyAccount(puuid: Puuid, region: Region): MyAccount = withContext(ioDispatcher) {
        val knownSummoner = addKnownSummoner(puuid, region)
        val entity = MyAccountEntity(knownSummoner.id)
        entity.id = storage.myAccountDAO().insert(entity)
        MyAccountImpl(entity)
    }

    override suspend fun deleteMyAccount(myAccount: MyAccount) {
        storage.myAccountDAO().getById(myAccount.id)?.let { storage.myAccountDAO().delete(it) } ?: throw NoDataFoundException()
        forgetSummonerById(myAccount.summonerId)
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

    override suspend fun getRiotAccountByGameName(gameName: String, tagLine: String, region: Region): RiotAccount {
        return withContext(ioDispatcher) {
            serviceFactory.getService<AccountV1Service>(region)
                .getByRiotId(gameName.noWhitespaces, tagLine.noWhitespaces)
                .let { dto ->
                    RiotAccount(
                        puuid = Puuid(dto.puuid),
                        region = region,
                        gameName = dto.gameName ?: gameName,
                        tagLine = dto.tagLine ?: tagLine,
                    )
                }
        }
    }

    override suspend fun getRiotAccountByPuuid(puuid: Puuid, region: Region): RiotAccount {
        return withContext(ioDispatcher) {
            serviceFactory.getService<AccountV1Service>(region)
                .getByPuuid(puuid.value).let { dto ->
                    RiotAccount(
                        puuid = Puuid(dto.puuid),
                        region = region,
                        gameName = dto.gameName!!,
                        tagLine = dto.tagLine!!,
                    )
                }
        }
    }

    override suspend fun requestRemoteSummonerByPuuidAndRegion(puuidAndRegion: PuuidAndRegion): Summoner {
        return summonerCacheByPuuid[puuidAndRegion]?.getIfValid(TTL) ?: withContext(computationDispatcher) { fetchSummonerByPuuid(puuidAndRegion) }.also { summoner ->
            val expiring = summoner.expiring
            summonerCacheByPuuid[puuidAndRegion] = expiring
        }
    }

    override suspend fun requestRemoteSummonerByGameNameAndTagLine(gameName: String, tagLine: String, region: Region): Summoner {
        return with(getRiotAccountByGameName(gameName, tagLine, region)) {
            requestRemoteSummonerByPuuidAndRegion(PuuidAndRegion(puuid, region))
        }
    }

    override suspend fun requestMasteriesByPuuidAndRegion(puuid: Puuid, region: Region): List<ChampionMastery> {
        return fetchChampionMasteriesByPuuid(MasteriesPuuidKey(puuid, region))
    }

    override suspend fun getMySummoners(): List<Summoner> = withContext(computationDispatcher) {
        withContext(ioDispatcher) { storage.summonerDAO().getMySummoners() }
            .map { summonerEntity ->
                val reg = Region.getById(summonerEntity.regionId)
                requestRemoteSummonerByPuuidAndRegion(PuuidAndRegion(Puuid(summonerEntity.puuid), reg))
            }
    }

    override suspend fun getMySummonersForRegion(reg: Region): List<Summoner> = withContext(ioDispatcher) {
        coroutineScope {
            storage.summonerDAO().getMySummonersByRegion(reg.id).map {
                async { requestRemoteSummonerByPuuidAndRegion(PuuidAndRegion(Puuid(it.puuid), Region.getById(it.regionId))) }
            }.awaitAll()
        }
    }

    override suspend fun requestSummonerByMyAccount(myAccount: MyAccount): Summoner = withContext(ioDispatcher) { requestKnownSummoner(myAccount.summonerId) }

    override suspend fun createFriend(myAccount: MyAccount, puuid: Puuid, region: Region) = withContext(ioDispatcher) {
        FriendEntity(myAccount.id, addKnownSummoner(puuid, region).id).let { entity ->
            Friend(storage.friendDAO().insert(entity), entity.myAccountId, entity.summonerId)
        }
    }

    override suspend fun getFriends(myAccount: MyAccount): List<Friend> = withContext(ioDispatcher) {
        storage.friendDAO().getByAccountId(myAccount.id).map { entity ->
            Friend(
                id = entity.id,
                myAccountId = entity.myAccountId,
                summonerId = entity.summonerId
            )
        }
    }

    override suspend fun deleteFriend(friend: Friend) = withContext(ioDispatcher) {
        with(storage.friendDAO()) { delete(getById(friend.id)) }
        forgetSummonerById(friend.summonerId)
    }

    override suspend fun requestSummonerForFriend(friend: Friend): Summoner = withContext(ioDispatcher) { requestKnownSummoner(friend.summonerId) }

    private suspend fun addKnownSummoner(puuid: Puuid, region: Region): SummonerInfo = withContext(ioDispatcher) {
        SummonerInfoImpl(
            id = storage.summonerDAO().insert(SummonerEntity(puuid = puuid.value, regionId = region.id)),
            puuid = puuid,
            region = region,
        )
    }

    private suspend fun forgetSummonerById(id: Long) = withContext(ioDispatcher) {
        storage.summonerDAO().getById(id)?.let { storage.summonerDAO().delete(it) } ?: throw NoDataFoundException()
    }

    private suspend fun requestKnownSummoner(summonerId: Long): Summoner {
        return storage.summonerDAO().getById(summonerId)?.run {
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

    private suspend fun fetchSummonerByPuuid(puuidAndRegion: PuuidAndRegion): Summoner {
        return createSummoner(requestManager.request(SummonerRequestByPuuid(PuuidKey(puuidAndRegion))).summonerDto, puuidAndRegion.region)
    }

    private fun createSummoner(dto: SummonerDto, region: Region): Summoner {
        return with(dto) {
            Summoner(
                region = region,
                iconId = profileIconId,
                puuid = Puuid(puuid),
                level = summonerLevel,
            )
        }
    }

    // Classes

    private data class PuuidKey(
        val puuidAndRegion: PuuidAndRegion,
    ) : RequestKey

    private data class MasteriesPuuidKey(
        val puuid: Puuid,
        val region: Region,
    ) : RequestKey

    private inner class ChampionMasteriesByPuuid(key: MasteriesPuuidKey) :
        Request<MasteriesPuuidKey, ChampionMasteriesRequestResult>(key) {

        override suspend fun invoke(): ChampionMasteriesRequestResult {
            val championMasteryV4 = serviceFactory.getService<ChampionMasteryV4>(key.region)
            return ChampionMasteriesRequestResult(championMasteryV4.getByPuuid(key.puuid.value))
        }
    }

    private data class ChampionMasteriesRequestResult(
        val championMasteryDtos: List<ChampionMasteryDto>
    ) : com.tsarsprocket.reportmid.requestManagerApi.data.RequestResult

    private inner class SummonerRequestByPuuid(key: PuuidKey) : Request<PuuidKey, SummonerRequestResult>(key) {

        override suspend fun invoke(): SummonerRequestResult {
            val summonerV4Service = serviceFactory.getService<SummonerV4Service>(key.puuidAndRegion.region)
            return SummonerRequestResult(summonerV4Service.getByPuuid(key.puuidAndRegion.puuid.value))
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