package com.tsarsprocket.reportmid.model

import android.content.Context
import android.util.Log
import androidx.annotation.WorkerThread
import com.merakianalytics.orianna.Orianna
import com.merakianalytics.orianna.types.common.GameMode
import com.merakianalytics.orianna.types.common.GameType
import com.merakianalytics.orianna.types.common.Queue
import com.tsarsprocket.reportmid.RIOTIconProvider
import com.tsarsprocket.reportmid.appApi.di.AppContext
import com.tsarsprocket.reportmid.baseApi.di.AppScope
import com.tsarsprocket.reportmid.dataDragonApi.data.DataDragon
import com.tsarsprocket.reportmid.di.assisted.CurrentMatchModelFactory
import com.tsarsprocket.reportmid.di.assisted.MatchHistoryModelFactory
import com.tsarsprocket.reportmid.lol.model.Champion
import com.tsarsprocket.reportmid.lol.model.Puuid
import com.tsarsprocket.reportmid.lol.model.PuuidAndRegion
import com.tsarsprocket.reportmid.lol.model.Region
import com.tsarsprocket.reportmid.lolServicesApi.riotapi.ServiceFactory
import com.tsarsprocket.reportmid.model.my_friend.MyFriendModel
import com.tsarsprocket.reportmid.room.MainDatabase
import com.tsarsprocket.reportmid.room.MyFriendEntity
import com.tsarsprocket.reportmid.stateApi.data.StateRepository
import com.tsarsprocket.reportmid.summonerApi.model.MyAccount
import com.tsarsprocket.reportmid.summonerApi.model.Summoner
import com.tsarsprocket.reportmid.summonerApi.model.puuidAndRegion
import com.tsarsprocket.reportmid.tools.Optional
import com.tsarsprocket.reportmid.tools.Optional.Companion.optional
import com.tsarsprocket.reportmid.utils.annotations.Temporary
import com.tsarsprocket.reportmid.utils.common.logError
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.ReplaySubject
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.rx2.rxObservable
import kotlinx.coroutines.rx2.rxSingle
import java.io.InputStreamReader
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Provider
import com.merakianalytics.orianna.types.common.Region as OriannaRegion
import com.merakianalytics.orianna.types.core.staticdata.Champion as OriannaChampion
import com.tsarsprocket.reportmid.lolServicesApi.R as RServices

const val PUUID_NONE = "com.tsarsprocket.reportmid.model.RepositoryKt.PUUID_NONE"

@AppScope
class Repository @Inject constructor(
    @AppContext
    val context: Context,
    databaseProvider: Provider<MainDatabase>,
    val iconProvider: RIOTIconProvider,
    val serviceFactory: ServiceFactory,
    private val summonerRepository: com.tsarsprocket.reportmid.summonerApi.data.SummonerRepository, // temporary
    private val stateRepository: StateRepository,
    private val currentMatchModelFactory: CurrentMatchModelFactory,
    private val matchHistoryModelFactory: MatchHistoryModelFactory,
    private val dataDragonProvider: Provider<DataDragon>
) {

    val initialized = ReplaySubject.createWithSize<Boolean>(1)

    lateinit var database: MainDatabase

    val summoners = ConcurrentHashMap<PuuidAndRegion, Summoner>()
    lateinit var dataDragon: DataDragon

    init {
        Observable.fromCallable {
            try {
                Log.d(Repository::class.simpleName, "Initializing...")

                database = databaseProvider.get()

                @Temporary runBlocking {
                    if(stateRepository.getActiveCurrentAccountId() == null) {

                        summonerRepository.getAllMyAccounts().firstOrNull()?.let { myAccount ->
                            stateRepository.setActiveCurrentAccountId(myAccount.id)
                        }
                    }
                }

                dataDragon = dataDragonProvider.get()

                Orianna.setRiotAPIKey(loadRawResourceAsText(RServices.raw.riot_api_key))
                Orianna.setDefaultRegion(OriannaRegion.RUSSIA)

                Log.d(Repository::class.simpleName, "Done initialize")

                return@fromCallable true
            } catch(ex: Exception) {
                logError("Error initializing the repository", ex)
                return@fromCallable false
            }
        }.flatMapSingle {
            rxSingle {
                dataDragon.waitForInitialization()
                it
            }
        }.subscribeOn(Schedulers.io()).subscribe(initialized)
    }

    fun getSelectedAccountRegion(): Observable<Region> {
        return ensureInitializedDoOnIOSubject {}.switchMap {
            @Temporary rxObservable {
                stateRepository.getActiveCurrentAccountId()?.let { myAccointId ->
                    send(stateRepository.getCurrentAccountById(myAccointId).region)
                }
            }
        }
    }

    fun getMyRegions(): Observable<List<Region>> = ensureInitializedDoOnIO { }
        .map { _ -> @Temporary runBlocking { summonerRepository.getAllMyAccounts() } }
        .map { lstMyAccs ->
            lstMyAccs.map { myAcc ->
                @Temporary runBlocking { summonerRepository.getSummonerInfoById(myAcc.summonerId).region }
            }
                .distinct()
        }

    private fun loadRawResourceAsText(resId: Int) =
        InputStreamReader(context.resources.openRawResource(resId)).readText()

    @WorkerThread
    fun findSummonerForName(
        summonerName: String,
        regionModel: Region,
        failOnNotFound: Boolean = false
    ): Observable<Summoner> {

        return initialized.observeOn(Schedulers.io())
            .switchMapSingle { fInitialized ->
                when(fInitialized) {
                    true -> rxSingle { summonerRepository.requestRemoteSummonerByName(summonerName, regionModel) }
                    else -> throw RepositoryNotInitializedException()
                }
            }
    }

    @WorkerThread
    fun getActiveSummonerPuuidAndRegion(): Observable<PuuidAndRegion> =
        initialized.observeOn(Schedulers.io())
            .switchMap { fInitialized ->
                when(fInitialized) {
                    true -> {
                        rxObservable {
                            stateRepository.getActiveCurrentAccountId()?.let { curAccId ->
                                val accId = stateRepository.getCurrentAccountById(curAccId).myAccountId
                                val curSummonerId = summonerRepository.getMyAccountById(accId).summonerId
                                send(summonerRepository.getSummonerInfoById(curSummonerId).puuidAndRegion)
                            }
                        }
                    }
                    else -> throw RepositoryNotInitializedException()
                }
            }

    fun getChampionModel(lmdChampion: () -> OriannaChampion) =
        ensureInitializedDoOnIOSubject { getChampionById(lmdChampion().id).blockingGet() }

    fun getChampionById(id: Int): Single<Champion> =
        ensureInitializedDoOnIO { dataDragon.tail.getChampionById(id.toLong()) }.firstOrError()

    fun getMatchHistoryModel(region: Region, summoner: Summoner): MatchHistoryModel =
        matchHistoryModelFactory.create(region, summoner)

    fun getCurrentMatch(summoner: Summoner) =
        ensureInitializedDoOnIOSubject { currentMatchModelFactory.create(summoner) }

    fun getMyAccounts(): Observable<List<MyAccount>> = ensureInitializedDoOnIO {
        @Temporary runBlocking { summonerRepository.getAllMyAccounts() }
    }

    fun getMyAccountForSummoner(summonerPuuid: Puuid, summonerRegion: Region): Single<MyAccount> {
        return ensureInitializedDoOnIO {}.firstOrError().map {
            runBlocking { summonerRepository.getMyAccountByPuuidAndRegion(PuuidAndRegion(summonerPuuid, summonerRegion)) }
        }.subscribeOn(Schedulers.io()).cache()
    }

    fun getSummonerForMyAccount(myAccount: MyAccount): Single<Summoner> =
        ensureInitializedDoOnIO {}
            .firstOrError()
            .map {
                @Temporary runBlocking {
                    val summonerInfo = summonerRepository.getSummonerInfoById(myAccount.summonerId)
                    summonerRepository.requestRemoteSummonerByPuuidAndRegion(summonerInfo.puuidAndRegion)
                }
            }

    fun getMyCurrentAccountsPerRegionObservable(reg: Region): Observable<Optional<MyAccount>> {
        return ensureInitializedDoOnIO {
            @Temporary runBlocking {
                try {
                    val currentAccount = stateRepository.getCurrentAccountByRegion(reg)
                    summonerRepository.getMyAccountById(currentAccount.myAccountId).optional()
                } catch(exception: Exception) {
                    Optional.empty()
                }
            }
        }
    }

    fun getFriendsForAcc(myAcc: MyAccount): ReplaySubject<List<MyFriendModel>> =
        ReplaySubject.create<List<MyFriendModel>>().also { subj ->
            ensureInitializedDoOnIO {}
                .switchMap {
                    database.myFriendDAO().getByAccountIdObservable(myAcc.id)
                        .map { lst -> lst.map { MyFriendModel(this, it.id) } }
                }.subscribe(subj)
        }

    fun getSummonerForFriend(myFriend: MyFriendModel): Single<Summoner> =
        ensureInitializedDoOnIO {
            val friendEnt = database.myFriendDAO().getById(myFriend.id)
            @Temporary runBlocking { summonerRepository.getSummonerInfoById(friendEnt.summonerId) }
        }
            .switchMapSingle { summonerInfo ->
                rxSingle { summonerRepository.requestRemoteSummonerByPuuidAndRegion(summonerInfo.puuidAndRegion) }
            }
            .firstOrError()

    //  Operations  ///////////////////////////////////////////////////////////

    /**
     * @return <code>Observable&lt;Boolean&gt;</code> that always fires <code>true</code>
     * @throws <code>RepositoryNotInitializedException</code>
     */
    fun addMyAccountNotify(
        summoner: Summoner,
        setCurrent: Boolean = false
    ): Observable<Boolean> {
        return initialized.observeOn(Schedulers.io())
            .doOnNext { fInitialized ->
                when(fInitialized) {
                    true -> {
                        @Temporary runBlocking {
                            val summonerId = summonerRepository.addKnownSummoner(summoner.puuid, summoner.region).id
                            val myAccount = summonerRepository.createMyAccount(summonerId = summonerId)
                            val currentId = stateRepository.setCurrentAccountIdByRegion(summoner.region, myAccount.id).id

                            if(setCurrent) {
                                stateRepository.setActiveCurrentAccountId(currentId)
                            }
                        }
                    }

                    else -> throw RepositoryNotInitializedException()
                }
            }
    }

    fun deleteMySummonersSwitchActive(summoners: List<Summoner>): ReplaySubject<List<String>> =
        ensureInitializedDoOnIOSubject {
            val newCurrentPuuids: MutableList<String> = MutableList(0) { throw RuntimeException() }
            database.runInTransaction {
                @Temporary runBlocking {
                    val mySumsIds = summoners.map {
                        summonerRepository.getKnownSummonerId(PuuidAndRegion(it.puuid, Region.getByTag(it.region.tag)))
                    }
                    val myAccs = mySumsIds.map { summonerRepository.getMyAccountBySummonerId(it) }

                    if(summonerRepository.getNumberOfMyAccounts() <= myAccs.size) throw RuntimeException("At least 1 account should be left")

                    val curAccs = myAccs.mapNotNull { stateRepository.getCurrentAccountByMyAccountId(it.id) }
                    curAccs.forEach { curAcc ->
                        val accsInRegion = summonerRepository.getMyAccountsByRegion(Region.getById(curAcc.region.id))
                        val newActiveAcc = accsInRegion.subtract(myAccs).firstOrNull()
                        @Temporary runBlocking {
                            if(newActiveAcc != null) {
                                stateRepository.setCurrentAccountIdByRegion(Region.getById(curAcc.region.id), newActiveAcc.id)
                                newCurrentPuuids.add(summonerRepository.getSummonerInfoById(newActiveAcc.summonerId).puuid.value)
                            } else {
                                stateRepository.deleteCurrentAccount(curAcc)
                            }
                        }
                    }
                    myAccs.forEach { myAccount -> summonerRepository.deleteMyAccount(myAccount) }
                    mySumsIds.forEach { summonerRepository.forgetSummonerById(it) }
                }
            }
            newCurrentPuuids
        }

    fun activateAccount(acc: MyAccount) = @Temporary runBlocking {
        val accEnt = summonerRepository.getMyAccountById(acc.id)
        val puuidAndRegion = summonerRepository.getSummonerInfoById(accEnt.summonerId)
        stateRepository.setCurrentAccountIdByRegion(puuidAndRegion.region, acc.id)
    }

    fun checkSummonerInUse(summoner: Summoner): Observable<Boolean> = ensureInitializedDoOnIO {
        @Temporary runBlocking { summonerRepository.isSummonerKnown(summoner.puuidAndRegion) }
    }

    fun createFriend(friend: PuuidAndRegion, mine: PuuidAndRegion): Observable<Boolean> =
        ensureInitializedDoOnIOSubject {
            try {
                database.runInTransaction {
                    @Temporary runBlocking {
                        with(database) {
                            val friendsSumId = summonerRepository.addKnownSummoner(friend.puuid, friend.region).id
                            val mySummonerId = summonerRepository.getKnownSummonerId(mine)
                            val myAcc = summonerRepository.getMyAccountBySummonerId(mySummonerId)
                            myFriendDAO().insert(MyFriendEntity(myAcc.id, friendsSumId))
                        }
                    }
                }
                true
            } catch(ex: Exception) {
                false
            }
        }

    fun deleteFriendsAndSummoners(friends: List<MyFriendModel>) = ensureInitializedDoOnIOSubject {
        try {
            database.runInTransaction {
                with(database) {
                    friends.forEach {
                        val friendEnt = myFriendDAO().getById(it.id)
                        myFriendDAO().delete(friendEnt)
                        @Temporary runBlocking { summonerRepository.forgetSummonerById(friendEnt.summonerId) }
                    }
                }
            }

            true
        } catch(ex: Exception) {
            false
        }
    }

    //  Static Methods  ///////////////////////////////////////////////////////

    companion object {
        val allRegions = Region.values()

        fun getGameType(
            gameType: GameType? = null,
            queue: Queue? = null,
            gameMode: GameMode? = null,
            gameMap: GameMap? = null
        ) =
            GameTypeModel.by(gameType, queue, gameMode, gameMap)
    }

    /*  Tools  ***************************************************************/

    private fun <T> ensureInitializedDoOnIOSubject(
        failOnNull: Boolean = false,
        l: () -> T?
    ): ReplaySubject<T> =
        ReplaySubject.createWithSize<T>(1)
            .also { subject -> ensureInitializedDoOnIO(failOnNull, l).subscribe(subject) }

    private fun <T> ensureInitializedDoOnIO(
        failOnNull: Boolean = false,
        l: () -> T?
    ): Observable<T> =
        initialized.observeOn(Schedulers.io()).switchMap { fInitialized ->
            when(fInitialized) {
                true -> {
                    val v = l()
                    if(v != null) Observable.just(v) else if(failOnNull) throw NullPointerException() else Observable.empty()
                }

                else -> Observable.error(RepositoryNotInitializedException())
            }
        }
}
