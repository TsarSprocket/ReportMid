package com.tsarsprocket.reportmid.model

import android.content.Context
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.room.Room
import com.merakianalytics.orianna.Orianna
import com.merakianalytics.orianna.types.common.GameMode
import com.merakianalytics.orianna.types.common.GameType
import com.merakianalytics.orianna.types.common.Queue
import com.merakianalytics.orianna.types.core.staticdata.Champion
import com.tsarsprocket.reportmid.RIOTIconProvider
import com.tsarsprocket.reportmid.base.di.AppScope
import com.tsarsprocket.reportmid.data_dragon.model.DataDragonImpl
import com.tsarsprocket.reportmid.di.assisted.CurrentMatchModelFactory
import com.tsarsprocket.reportmid.di.assisted.MatchHistoryModelFactory
import com.tsarsprocket.reportmid.lol.model.PuuidAndRegion
import com.tsarsprocket.reportmid.lol.model.Region
import com.tsarsprocket.reportmid.lol_services_api.riotapi.ServiceFactory
import com.tsarsprocket.reportmid.model.my_account.MyAccountModel
import com.tsarsprocket.reportmid.model.my_friend.MyFriendModel
import com.tsarsprocket.reportmid.room.MainStorage
import com.tsarsprocket.reportmid.room.MyAccountEntity
import com.tsarsprocket.reportmid.room.MyFriendEntity
import com.tsarsprocket.reportmid.room.SummonerEntity
import com.tsarsprocket.reportmid.room.state.CurrentAccountEntity
import com.tsarsprocket.reportmid.room.state.GlobalEntity
import com.tsarsprocket.reportmid.summoner.model.SummonerModel
import com.tsarsprocket.reportmid.summoner.model.SummonerRepository
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.ReplaySubject
import java.io.InputStreamReader
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import com.merakianalytics.orianna.types.common.Region as OriannaRegion
import com.tsarsprocket.reportmid.lol_services_api.R as RServices

const val PUUID_NONE = "com.tsarsprocket.reportmid.model.RepositoryKt.PUUID_NONE"

@AppScope
class Repository @Inject constructor(
    val context: Context,
    val iconProvider: RIOTIconProvider,
    val serviceFactory: ServiceFactory,
    private val summonerRepository: SummonerRepository,
    private val currentMatchModelFactory: CurrentMatchModelFactory,
    private val matchHistoryModelFactory: MatchHistoryModelFactory,
) {

    val initialized = ReplaySubject.createWithSize<Boolean>(1)

    lateinit var database: MainStorage

    val summoners = ConcurrentHashMap<PuuidAndRegion, SummonerModel>()
    lateinit var dataDragon: DataDragonImpl

    init {

        Observable.fromCallable {
            try {
                Log.d(Repository::class.simpleName, "Initializing...")

                database = Room.databaseBuilder(context.applicationContext, MainStorage::class.java, "database").createFromAsset("database/init.db").build()

                val stateList = database.globalDAO().getAll()

                if (stateList.isEmpty()) {

                    val accs = database.myAccountDAO().getAll()

                    database.globalDAO().insert(GlobalEntity(accs.firstOrNull()?.id))
                }

                dataDragon = DataDragonImpl(database, iconProvider)

                Orianna.setRiotAPIKey(loadRawResourceAsText(RServices.raw.riot_api_key))
                Orianna.setDefaultRegion(OriannaRegion.RUSSIA)

                Log.d(Repository::class.simpleName, "Done initialize")

                return@fromCallable true
            } catch (ex: Exception) {
                return@fromCallable false
            }
        }.subscribeOn(Schedulers.io()).subscribe(initialized)
    }

    fun getSelectedAccountRegion() = ensureInitializedDoOnIOSubject {
        database.globalDAO().getAll()
            .map { database.currentAccountDAO().getById(it.currentAccountId!!) }
            .map { database.regionDAO().getById(it.regionId) }
            .first()
    }.map { Region.getByTag(it.tag) }

    fun getMyRegions(): Observable<List<Region>> = ensureInitializedDoOnIO { }
        .switchMap { _ -> database.myAccountDAO().getAllObservable() }
        .map { lstMyAccs ->
            lstMyAccs.map { myAcc ->
                val sum = database.summonerDAO().getById(myAcc.summonerId)
                database.regionDAO().getById(sum.regionId)
            }
                .distinct()
                .map { regEnt -> Region.byTag[regEnt.tag] ?: throw RuntimeException("Region not found for tag ${regEnt.tag}") }
        }

    private fun loadRawResourceAsText(resId: Int) = InputStreamReader(context.resources.openRawResource(resId)).readText()

    @WorkerThread
    fun findSummonerForName(summonerName: String, regionModel: Region, failOnNotFound: Boolean = false): Observable<SummonerModel> {

        return initialized.observeOn(Schedulers.io())
            .switchMapSingle { fInitialized ->
                when (fInitialized) {
                    true -> summonerRepository.getBySummonerName(summonerName, regionModel)
                    else -> throw RepositoryNotInitializedException()
                }
            }
    }

    @WorkerThread
    fun getActiveSummonerPuuidAndRegion(): Observable<PuuidAndRegion> = initialized.observeOn(Schedulers.io())
        .switchMap { fInitialized ->
            when( fInitialized ) {
                true -> {
                    val curAccId = database.globalDAO().getAll().first().currentAccountId

                    if (curAccId != null) {
                        val accId = database.currentAccountDAO().getById(curAccId).accountId
                        val curSummonerId = database.myAccountDAO().getById(accId).summonerId
                        val sum = database.summonerDAO().getById(curSummonerId)
                        val regEnt =database.regionDAO().getById(sum.regionId)
                        Observable.just(PuuidAndRegion(sum.puuid, Region.getByTag(regEnt.tag)))
                    } else Observable.empty()
                }
                else -> throw RepositoryNotInitializedException()
            }
        }

    fun getChampionModel(lmdChampion: () -> Champion) = ensureInitializedDoOnIOSubject { getChampionById(lmdChampion().id).blockingGet() }

    fun getChampionById(id: Int): Single<ChampionModel> =
        ensureInitializedDoOnIO { dataDragon.tail.getChampionById(id) }.firstOrError()

    fun getMatchHistoryModel(region: Region, summoner: SummonerModel): MatchHistoryModel = matchHistoryModelFactory.create(region, summoner)

    fun getCurrentMatch(summoner: SummonerModel) = ensureInitializedDoOnIOSubject { currentMatchModelFactory.create( summoner ) }

    fun getMyAccounts(): Observable<List<MyAccountModel>> = ensureInitializedDoOnIO {
        database.myAccountDAO().getAll()
    }.map { list -> list.map { MyAccountModel(this,it.id) } }

    fun getMyAccountById(id: Long): ReplaySubject<MyAccountModel> = ReplaySubject.create<MyAccountModel>(1).also { subj ->
            Observable.just(MyAccountModel(this,id)).subscribe(subj)
    }

    fun getMyAccountForSummoner(summoner: SummonerModel): Maybe<MyAccountModel> = ensureInitializedDoOnIO {}.firstElement().flatMap {
        Maybe.fromCallable {
            val myAccEnt =
                database.summonerDAO().getByPuuidAndRegionTag(summoner.puuid, summoner.region.tag)?.let { database.myAccountDAO().getBySummonerId(it.id) }
                    ?: throw RuntimeException("No My Account for summoner ")
            MyAccountModel(this, myAccEnt.id)
        }
    }.subscribeOn(Schedulers.io()).cache()

    fun getSummonerForMyAccount(myAccount: MyAccountModel): Single<SummonerModel> = ensureInitializedDoOnIO {}.flatMapSingle {
        val sumEnt = database.summonerDAO().getById(database.myAccountDAO().getById(myAccount.id).summonerId)
        summonerRepository.getByPuuidAndRegion(PuuidAndRegion(sumEnt.puuid,database.regionDAO().getById(sumEnt.regionId).tag))
    }.firstOrError()

    fun getCurrentAccountsPerRegionObservable(reg: Region): Observable<List<MyAccountModel>> = ensureInitializedDoOnIO { }
        .switchMap { database.currentAccountDAO().getByRegionIdObservable(database.regionDAO().getByTag(reg.tag).id) }
        .map { list -> list.map { curAccEnt -> MyAccountModel(this, curAccEnt.accountId) } }

    fun getFriendsForAcc(myAcc: MyAccountModel): ReplaySubject<List<MyFriendModel>> = ReplaySubject.create<List<MyFriendModel>>().also { subj ->
        ensureInitializedDoOnIO {}
            .switchMap {
                database.myFriendDAO().getByAccountIdObservable(myAcc.id).map { lst -> lst.map{ MyFriendModel(this, it.id) } }
            }.subscribe(subj)
    }

    fun getSummonerForFriend(myFriend: MyFriendModel): Single<SummonerModel> = ensureInitializedDoOnIO {
        val friendEnt = database.myFriendDAO().getById(myFriend.id)
        val sumEnt = database.summonerDAO().getById(friendEnt.summonerId)
        val regEnt = database.regionDAO().getById(sumEnt.regionId)
        Pair(sumEnt,regEnt)
    }
        .switchMapSingle { (sumEnt, regEnt) -> summonerRepository.getByPuuidAndRegion(PuuidAndRegion(sumEnt.puuid,regEnt.tag)) }
        .firstOrError()

    //  Operations  ///////////////////////////////////////////////////////////

    /**
     * @return <code>Observable&lt;Boolean&gt;</code> that always fires <code>true</code>
     * @throws <code>RepositoryNotInitializedException</code>
     */
    fun addMyAccountNotify(summonerModel: SummonerModel, setCurrent: Boolean = false): Observable<Boolean> =
        initialized.observeOn(Schedulers.io())
            .doOnNext { fInitialized ->
                when (fInitialized) {
                    true -> {
                        val regionEntity = database.regionDAO().getByTag(summonerModel.region.tag)

                        val summonerEntity = SummonerEntity(summonerModel.puuid, regionEntity.id)

                        val sumId = database.summonerDAO().insert(summonerEntity)

                        val myAccountEntity = MyAccountEntity(sumId)

                        val accId = database.myAccountDAO().insert(myAccountEntity)

                        val current = database.currentAccountDAO().getByRegionId(regionEntity.id)

                        val currentId = if (current != null) {
                            if (current.accountId != accId && setCurrent) {
                                current.accountId = accId
                                database.currentAccountDAO().update(current)
                            }

                            current.id
                        } else {
                            database.currentAccountDAO().insert(CurrentAccountEntity(regionId = regionEntity.id, accountId = accId))
                        }

                        if (setCurrent) {
                            val globalState = database.globalDAO().getAll()[0]
                            globalState.currentAccountId = currentId
                            database.globalDAO().update(globalState)
                        }
                    }
                    else -> throw RepositoryNotInitializedException()
                }
            }

    fun deleteMySummonersSwitchActive( summoners: List<SummonerModel> ): ReplaySubject<List<String>> = ensureInitializedDoOnIOSubject {
        val newCurrentPuuids: MutableList<String> = MutableList(0) { throw RuntimeException() }
        database.runInTransaction {
            val mySums = summoners.mapNotNull { database.summonerDAO().getByPuuidAndRegionId(it.puuid,database.regionDAO().getByTag(it.region.tag).id) }
            val myAccs = mySums.mapNotNull { database.myAccountDAO().getBySummonerId(it.id) }

            if (database.myAccountDAO().count() <= myAccs.size) throw RuntimeException( "At least 1 account should be left" )

            val curAccs = myAccs.mapNotNull { database.currentAccountDAO().getByMyAccountId(it.id) }
            curAccs.forEach { curAcc ->
                val accsInRegion = database.myAccountDAO().getByRegion(curAcc.regionId)
                val newActiveAcc = accsInRegion.subtract( myAccs ).firstOrNull()
                if( newActiveAcc != null ) {
                    curAcc.accountId = newActiveAcc.id
                    database.currentAccountDAO().update(curAcc)
                    newCurrentPuuids.add( database.summonerDAO().getById(newActiveAcc.summonerId).puuid )
                } else {
                    database.currentAccountDAO().delete(curAcc)
                }
            }
            myAccs.forEach { database.myAccountDAO().delete(it) }
            mySums.forEach { database.summonerDAO().delete(it) }
        }
        newCurrentPuuids
    }

    fun activateAccount(acc: MyAccountModel) {
        val myAccEnt = database.myAccountDAO().getById(acc.id)
        val sumEnt = database.summonerDAO().getById(myAccEnt.summonerId)
        val curAccEnt = database.currentAccountDAO().getByRegionId(sumEnt.regionId)
        if (curAccEnt != null && acc.id != curAccEnt.accountId) {
            curAccEnt.accountId = acc.id
            database.currentAccountDAO().update(curAccEnt)
        }
    }

    fun checkSummonerExistInDB(summoner: SummonerModel): Observable<Boolean> = ensureInitializedDoOnIO {
        val sumEnt = database.summonerDAO().getByPuuidAndRegionId(summoner.puuid, database.regionDAO().getByTag(summoner.region.tag).id)
        sumEnt != null
    }

    fun createFriend(friend: PuuidAndRegion, mine: PuuidAndRegion) = ensureInitializedDoOnIOSubject {
        try {
            database.runInTransaction {
                with(database) {
                    val friendsSumId = summonerDAO().insert(SummonerEntity(friend.puuid, regionDAO().getByTag(friend.region.tag).id))
                    val mySumEnt = summonerDAO().getByPuuidAndRegionId(mine.puuid, regionDAO().getByTag(mine.region.tag).id)!!
                    val myAcc = myAccountDAO().getBySummonerId(mySumEnt.id)!!
                    myFriendDAO().insert(MyFriendEntity(myAcc.id, friendsSumId))
                }
            }

            true
        } catch (ex: Exception) { false }
    }

    fun deleteFriendsAndSummoners(friends: List<MyFriendModel>) = ensureInitializedDoOnIOSubject {
        try {
            database.runInTransaction {
                with(database) {
                    friends.forEach {
                        val friendEnt = myFriendDAO().getById(it.id)
                        val sumEnt = summonerDAO().getById(friendEnt.summonerId)
                        myFriendDAO().delete(friendEnt)
                        summonerDAO().delete(sumEnt)
                    }
                }
            }

            true
        } catch(ex: Exception) { false }
    }

    //  Static Methods  ///////////////////////////////////////////////////////

    companion object {
        val allRegions = Region.values()

        fun getGameType(gameType: GameType? = null, queue: Queue? = null, gameMode: GameMode? = null, gameMap: GameMap? = null) =
            GameTypeModel.by(gameType, queue, gameMode, gameMap)
    }

    /*  Tools  ***************************************************************/

    private fun <T> ensureInitializedDoOnIOSubject(failOnNull: Boolean = false, l: () -> T?): ReplaySubject<T> =
        ReplaySubject.createWithSize<T>(1).also { subject -> ensureInitializedDoOnIO(failOnNull, l).subscribe(subject) }

    private fun <T> ensureInitializedDoOnIO(failOnNull: Boolean = false, l: () -> T?): Observable<T> =
        initialized.observeOn(Schedulers.io()).switchMap { fInitialized ->
            when (fInitialized) {
                true -> {
                    val v = l()
                    if (v != null) Observable.just(v) else if (failOnNull) throw NullPointerException() else Observable.empty()
                }
                else -> Observable.error(RepositoryNotInitializedException())
            }
        }
}
