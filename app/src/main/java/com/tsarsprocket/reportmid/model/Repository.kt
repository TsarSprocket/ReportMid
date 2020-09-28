package com.tsarsprocket.reportmid.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.room.Room
import com.merakianalytics.orianna.Orianna
import com.merakianalytics.orianna.types.common.*
import com.merakianalytics.orianna.types.common.Queue
import com.merakianalytics.orianna.types.core.championmastery.ChampionMastery
import com.merakianalytics.orianna.types.core.league.LeagueEntry
import com.merakianalytics.orianna.types.core.match.*
import com.merakianalytics.orianna.types.core.spectator.CurrentMatch
import com.merakianalytics.orianna.types.core.spectator.CurrentMatchTeam
import com.merakianalytics.orianna.types.core.spectator.Player
import com.merakianalytics.orianna.types.core.spectator.Runes
import com.merakianalytics.orianna.types.core.staticdata.Champion
import com.merakianalytics.orianna.types.core.staticdata.Item
import com.merakianalytics.orianna.types.core.staticdata.ReforgedRune
import com.merakianalytics.orianna.types.core.staticdata.SummonerSpell
import com.merakianalytics.orianna.types.core.summoner.Summoner
import com.tsarsprocket.reportmid.R
import com.tsarsprocket.reportmid.room.state.GlobalEntity
import com.tsarsprocket.reportmid.room.MainStorage
import com.tsarsprocket.reportmid.room.MyAccountEntity
import com.tsarsprocket.reportmid.room.SummonerEntity
import com.tsarsprocket.reportmid.room.state.CurrentAccountEntity
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.ReplaySubject
import java.io.InputStreamReader
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

const val PUUID_NONE = "com.tsarsprocket.reportmid.model.RepositoryKt.PUUID_NONE"

@Singleton
class Repository @Inject constructor(val context: Context) {

    val initialized = ReplaySubject.createWithSize<Boolean>(1)

    lateinit var database: MainStorage

    val summoners = ConcurrentHashMap<PuuidAndRegion, SummonerModel>()
    val champions = ConcurrentHashMap<Int, ChampionModel>()
    val summonerSpells = ConcurrentHashMap<Int, SummonerSpellModel>()
    val runes = ConcurrentHashMap<Int, RuneModel>()

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

//                  Orianna.loadConfiguration( CharSource.wrap( loadOriannaConfigToString() ) )
                Orianna.setRiotAPIKey(loadRawResourceAsText(R.raw.riot_api_key))
                Orianna.setDefaultRegion(Region.RUSSIA)

                Log.d(Repository::class.simpleName, "Done initialize")

                return@fromCallable true
            } catch (ex: Exception) {

                return@fromCallable false
            }
        }.subscribeOn(Schedulers.io()).subscribe(initialized)
    }

    fun getSelectedAccountSummoner() = ensureInitializedDoOnIOSubject {
        database.globalDAO().getAll()
            .map { database.currentAccountDAO().getById(it.currentAccountId!!) }
            .map { database.summonerDAO().getById(it.accountId) }
            .first()
    }.flatMap { sum ->
        val reg = database.regionDAO().getById(sum.regionId)
        findSummonerByPuuidAndRegion(PuuidAndRegion(sum.puuid,RegionModel.getByTag(reg.tag)))
    }

    fun getSelectedAccountRegion() = ensureInitializedDoOnIOSubject {
        database.globalDAO().getAll()
            .map { database.currentAccountDAO().getById(it.currentAccountId!!) }
            .map { database.regionDAO().getById(it.regionId) }
            .first()
    }.map { RegionModel.getByTag(it.tag) }

    fun getMyRegions(): Observable<List<RegionModel>> = ensureInitializedDoOnIO { Unit }
        .flatMap { _ -> database.myAccountDAO().getAllObservable() }
        .map { lstMyAccs ->
            lstMyAccs.map { myAcc ->
                val sum = database.summonerDAO().getById(myAcc.summonerId)
                database.regionDAO().getById(sum.regionId)
            }
                .distinct()
                .map { regEnt -> RegionModel.byTag[regEnt.tag] ?: throw RuntimeException("Region not found for tag ${regEnt.tag}") }
        }

    fun getCurrentRegions() = ensureInitializedDoOnIOSubject { Unit }
        .flatMap { _ ->
            database.currentAccountDAO().getAllObservable()
                .map { lstCurAccEnt ->
                    lstCurAccEnt.map { database.regionDAO().getById(it.regionId) }
                        .map { RegionModel.byTag[it.tag] ?: throw RuntimeException("Region ${it.tag} not found") }
                }
        }


    fun getMySummonersSubject(): ReplaySubject<List<SummonerModel>> = ensureInitializedDoOnIOSubject {
        val arrObsSumModel = database.summonerDAO().getMySummoners()
            .map { sumEnt ->
                val reg = database.regionDAO().getById(sumEnt.regionId)
                findSummonerByPuuidAndRegion(PuuidAndRegion(sumEnt.puuid,RegionModel.getByTag(reg.tag)))
            }.toTypedArray()
        Observable.mergeArray(*arrObsSumModel).subscribeOn(Schedulers.io()).toList().blockingGet()
    }

    fun getMySummonersObservableForRegion(reg: RegionModel): Observable<List<Pair<SummonerModel, Boolean>>> = ensureInitializedDoOnIO {
        database.regionDAO().getByTag(reg.tag)
    }
        .flatMap { regEnt ->
            database.currentAccountDAO().getByRegionObservable(regEnt.id)
                .map { lstCurAcc ->
                    val firstOne = lstCurAcc.first()
                    Pair(regEnt, database.myAccountDAO().getById(firstOne.accountId))
                }
        }
        .flatMap { (regEnt, myCurAccEnt) ->
            database.summonerDAO().getMySummonersByRegionObservable(regEnt.id)
                .map { sumEnts -> sumEnts.map { sumEnt -> Pair(sumEnt, sumEnt.id == myCurAccEnt.summonerId) } }
                .map { lst ->
                    lst.map { (sumEnt, isSelected) ->
                        findSummonerByPuuidAndRegion(PuuidAndRegion(sumEnt.puuid,RegionModel.getByTag(regEnt.tag)))
                            .map { sum -> Pair(sum, isSelected) }.blockingFirst()
                    }
                }
        }

    private fun loadRawResourceAsText(resId: Int) = InputStreamReader(context.resources.openRawResource(resId)).readText()

    @WorkerThread
    fun findSummonerForName(summonerName: String, regionModel: RegionModel, failOnNotFound: Boolean = false): Observable<SummonerModel> {

        return initialized.observeOn(Schedulers.io())
            .flatMap { fInitialized ->
                when (fInitialized) {
                    true -> getSummonerModel(failOnNull = failOnNotFound) { Orianna.summonerNamed(summonerName).withRegion(regionModel.shadowRegion).get() }
                    else -> throw RepositoryNotInitializedException()
                }
            }
    }

    @WorkerThread
    fun findSummonerByPuuidAndRegion(puuidAndRegion: PuuidAndRegion): ReplaySubject<SummonerModel> {
        val cached = summoners[puuidAndRegion]
        return if (cached != null) ReplaySubject.create<SummonerModel>(1).apply { Observable.just(cached).subscribe(this) }
        else getSummonerModel { Orianna.summonerWithPuuid(puuidAndRegion.puuid).withRegion(puuidAndRegion.region.shadowRegion).get().also { it.load() } }
    }

    fun findSummonersByPuuidAndRegions(puuidAndRegionsList: List<PuuidAndRegion>) = ensureInitializedDoOnIOSubject {
        puuidAndRegionsList.map { puuidAndRegion -> summoners[puuidAndRegion] ?:
            SummonerModel(this, Orianna.summonerWithPuuid(puuidAndRegion.puuid).withRegion(puuidAndRegion.region.shadowRegion).get()) }
    }

    @WorkerThread
    fun getActiveSummonerPuuidAndRegion(): Observable<PuuidAndRegion> = initialized.observeOn(Schedulers.io())
        .flatMap { fInitialized ->
            when (fInitialized) {
                true -> {
                    val curAccId = database.globalDAO().getAll().first().currentAccountId

                    if (curAccId != null) {
                        val accId = database.currentAccountDAO().getById(curAccId).accountId
                        val curSummonerId = database.myAccountDAO().getById(accId).summonerId
                        val sum = database.summonerDAO().getById(curSummonerId)
                        val regEnt =database.regionDAO().getById(sum.regionId)
                        Observable.just(PuuidAndRegion(sum.puuid,RegionModel.getByTag(regEnt.tag)))
                    } else Observable.empty()
                }
                else -> throw RepositoryNotInitializedException()
            }
        }

    @WorkerThread
    fun getActiveSummoner() = getActiveSummonerPuuidAndRegion().flatMap { puuid -> findSummonerByPuuidAndRegion(puuid) }

    fun getChampionMasteryModel(championMastery: ChampionMastery) = ensureInitializedDoOnIOSubject { ChampionMasteryModel(this, championMastery) }

    fun getChampionModel(lmdChampion: () -> Champion) = ensureInitializedDoOnIOSubject {
        val champion = lmdChampion()
        champions[champion.id] ?: ChampionModel(this, champion).also { champions[it.id] = it }
    }

    fun getMatchHistoryModel(matchHistory: MatchHistory) = ensureInitializedDoOnIOSubject { MatchHistoryModel(this, matchHistory) }

    fun getMatchModel(match: Match) = ensureInitializedDoOnIOSubject { MatchModel(this, match) }

    fun getParticipantModel(teamModel: TeamModel, participant: Participant) = ensureInitializedDoOnIOSubject { ParticipantModel(this, teamModel, participant) }

    fun getSummonerModel(failOnNull: Boolean = false, lmdSummoner: () -> Summoner) = ensureInitializedDoOnIOSubject(failOnNull) {
        val summoner = lmdSummoner()
        if (summoner.puuid != null) {
            summoners[PuuidAndRegion(summoner.puuid,RegionModel.getByTag(summoner.region.tag))] ?:
                SummonerModel(this, summoner).also { summoners[PuuidAndRegion(it.puuid,it.region)] = it }
        } else null
    }

    fun getTeamModel(team: Team) = ensureInitializedDoOnIOSubject { TeamModel(this, team) }

    fun getItemModel(item: Item) = ensureInitializedDoOnIOSubject { ItemModel(this, item) }

    fun getSummonerSpell(lmdSummonerSpell: () -> SummonerSpell) = ensureInitializedDoOnIOSubject {
        val spell = lmdSummonerSpell()
        summonerSpells[spell.id] ?: SummonerSpellModel(this, spell).also { summonerSpells[spell.id] = it }
    }

    fun getRuneStats(runeStats: RuneStats) = ensureInitializedDoOnIOSubject { RuneStatsModel(this, runeStats) }

    fun getRune(lmdReforgedRune: () -> ReforgedRune) = ensureInitializedDoOnIOSubject {
        val reforgedRune = lmdReforgedRune()
        runes[reforgedRune.id] ?: RuneModel(this, reforgedRune).also { runes[reforgedRune.id] = it }
    }

    fun getCurrentMatch(lmdCurrentMatch: () -> CurrentMatch) = ensureInitializedDoOnIOSubject { CurrentMatchModel(this, lmdCurrentMatch()) }

    fun getCurrentMatchTeam(lmdCurrentMatchTeam: () -> CurrentMatchTeam) = ensureInitializedDoOnIOSubject { CurrentMatchTeamModel(this, lmdCurrentMatchTeam()) }

    fun getPlayer(lmdPlayer: () -> Player) = ensureInitializedDoOnIOSubject { PlayerModel(this, lmdPlayer()) }

    fun getPlayerRunes(lmdRunes: () -> Runes) = ensureInitializedDoOnIOSubject { PlayerRunesModel(this, lmdRunes()) }

    fun getLeaguePosition(lmdLeagueEntry: () -> LeagueEntry?) = ensureInitializedDoOnIOSubject { lmdLeagueEntry()?.let { LeaguePositionModel(this, it) } }

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

                        val current = database.currentAccountDAO().getByRegion(regionEntity.id)

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
            val myAccs = summoners.map { database.myAccountDAO().getById(database.summonerDAO().getByPuuid(it.puuid).id) }

            if (database.myAccountDAO().count() <= myAccs.size) throw RuntimeException( "At least 1 account should be left" )

            val curAccs = myAccs.mapNotNull { database.currentAccountDAO().getByMyAccount(it.id) }
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
        }
        newCurrentPuuids
    }


    //  Static Methods  ///////////////////////////////////////////////////////

    companion object {
        val allRegions = RegionModel.values()
        fun getRegion(region: Region) = RegionModel.byShadowRegion[region] ?: throw RuntimeException("No such region: ${region.tag}")
        fun getRunePath(pathId: Maybe<Int>) = if (pathId.isEmpty.blockingGet()) Maybe.empty() else Maybe.just(
            RunePathModel.byId[pathId.blockingGet()] ?: throw IncorrectRunePathIdException(pathId.blockingGet())
        )

        fun getGameType(gameType: GameType? = null, queue: Queue? = null, gameMode: GameMode? = null, gameMap: GameMap? = null) =
            GameTypeModel.by(gameType, queue, gameMode, gameMap)

        fun getSide(side: Side) = SideModel.fromExternal(side)
        fun getQueue(queue: Queue) =
            QueueModel.values().find { it.shadowQueue.id == queue.id } ?: throw RuntimeException("Queue $queue is not defined in the model")

        fun getTier(tier: Tier) = TierModel.values().find { it.shadowTier == tier } ?: throw RuntimeException("Tier $tier is not mapped")
        fun getDivision(division: Division) =
            DivisionModel.values().find { it.shadowDivision == division } ?: throw RuntimeException("Division $division is not mapped")

        @WorkerThread
        fun findParticipant(match: MatchModel, summoner: SummonerModel): ParticipantModel {
            val blueTeam = match.blueTeam.blockingSingle()
            val redTeam = match.redTeam.blockingSingle()

            val firstInBlue = blueTeam.participants.first().blockingSingle()
            if (firstInBlue.summoner.blockingSingle().puuid == summoner.puuid) return firstInBlue

            val firstInRed = redTeam.participants.first().blockingSingle()
            if (firstInRed.summoner.blockingSingle().puuid == summoner.puuid) return firstInRed

            return (
                    blueTeam.participants.subList(1, blueTeam.participants.size)
                        .union(redTeam.participants.subList(1, redTeam.participants.size))
                        .find { it.blockingSingle().summoner.blockingSingle().puuid == summoner.puuid }
                        ?: throw RuntimeException("Summoner ${summoner.name} is not found in match ${match.id}")
                    ).blockingSingle()
        }

        @WorkerThread
        fun getItemIcons(participant: ParticipantModel): Array<Bitmap> {
            val items = participant.items.blockingSingle()
            return Array(items.size) { i -> items[i].blockingSingle().bitmap.blockingSingle() }
        }

        @WorkerThread
        fun getTeamIcons(team: TeamModel, resizeMatrix: Matrix) = Array(team.participants.size) { i ->
            val bm = team.participants[i].blockingSingle().champion.blockingSingle().bitmap.blockingSingle()
            Bitmap.createBitmap(bm, 0, 0, bm.width, bm.height, resizeMatrix, false)
        }

        @WorkerThread
        fun calculateTeamKDA(asParticipant: ParticipantModel): Triple<Int, Int, Int> {
            var teamKills = 0
            var teamDeaths = 0
            var teamAssists = 0
            asParticipant.team.participants.forEach {
                val participant = it.blockingSingle()
                teamKills += participant.kills
                teamDeaths += participant.deaths
                teamAssists += participant.assists
            }
            return Triple(teamKills, teamDeaths, teamAssists)
        }
    }

    /*  Tools  ***************************************************************/

    private fun <T> ensureInitializedDoOnIOSubject(failOnNull: Boolean = false, l: () -> T?): ReplaySubject<T> =
        ReplaySubject.createWithSize<T>(1).also { subject -> ensureInitializedDoOnIO(failOnNull, l).subscribe(subject) }

    private fun <T> ensureInitializedDoOnIO(failOnNull: Boolean = false, l: () -> T?): Observable<T> =
        initialized.observeOn(Schedulers.io()).flatMap { fInitialized ->
            when (fInitialized) {
                true -> {
                    val v = l()
                    if (v != null) Observable.just(v) else if (failOnNull) throw NullPointerException() else Observable.empty()
                }
                else -> Observable.error(RepositoryNotInitializedException())
            }
        }
}
