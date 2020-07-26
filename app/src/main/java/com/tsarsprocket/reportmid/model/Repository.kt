package com.tsarsprocket.reportmid.model

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.room.Room
import com.merakianalytics.orianna.Orianna
import com.merakianalytics.orianna.types.common.GameMode
import com.merakianalytics.orianna.types.common.GameType
import com.merakianalytics.orianna.types.common.Queue
import com.merakianalytics.orianna.types.common.Region
import com.merakianalytics.orianna.types.core.championmastery.ChampionMastery
import com.merakianalytics.orianna.types.core.match.*
import com.merakianalytics.orianna.types.core.staticdata.Champion
import com.merakianalytics.orianna.types.core.staticdata.Item
import com.merakianalytics.orianna.types.core.staticdata.ReforgedRune
import com.merakianalytics.orianna.types.core.staticdata.SummonerSpell
import com.merakianalytics.orianna.types.core.summoner.Summoner
import com.tsarsprocket.reportmid.R
import com.tsarsprocket.reportmid.room.GlobalStateEntity
import com.tsarsprocket.reportmid.room.MainStorage
import com.tsarsprocket.reportmid.room.SummonerEntity
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.RuntimeException
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor( val context: Context ) {

    val initialized: Observable<Boolean>

    lateinit var database: MainStorage

    val summoners = ConcurrentHashMap<String,SummonerModel>()
    val champions = ConcurrentHashMap<Int,ChampionModel>()
    val summonerSpells = ConcurrentHashMap<Int,SummonerSpellModel>()
    val runes = ConcurrentHashMap<Int,RuneModel>()

    init {

        initialized = Observable.fromCallable {
            try {
                Log.d( Repository::class.simpleName, "Initializing..." )

                database = Room.databaseBuilder( context.applicationContext, MainStorage::class.java, "database" ).build()

                val stateList = database.globalStateDAO().getAll()

                if( stateList.isEmpty() ) {

                    val sums = database.summonerDAO().getAll()

                    database.globalStateDAO().insert( GlobalStateEntity( if( sums.isNotEmpty() ) sums[ 0 ].id else -1 ) )
                }

//                  Orianna.loadConfiguration( CharSource.wrap( loadOriannaConfigToString() ) )
                Orianna.setRiotAPIKey( loadRawResourceAsText(R.raw.riot_api_key) )
                Orianna.setDefaultRegion( Region.RUSSIA )

                Log.d( Repository::class.simpleName, "Done initialize" )

                return@fromCallable true
            } catch ( ex: Exception ) {

                return@fromCallable false
            }
        }.subscribeOn( Schedulers.io() ).replay( 1 ).autoConnect()
    }

    private fun loadRawResourceAsText( resId: Int ) = InputStreamReader( context.resources.openRawResource( resId ) ).readText()

    val allRegions = RegionModel.values()

    @WorkerThread
    fun findSummonerForName( summonerName: String, regionModel: RegionModel ): Observable<SummonerModel> {

        return initialized.observeOn( Schedulers.io() )
            .flatMap { fInitialized ->
                when( fInitialized ) {
                    true -> getSummonerModel( Orianna.summonerNamed( summonerName ).withRegion( regionModel.shadowRegion ).get() )
                    else -> throw RepositoryNotInitializedException()
                }
            }
    }

    @WorkerThread
    fun findSummonerByPuuid( puuid: String? ): Observable<SummonerModel> {

        return initialized.observeOn( Schedulers.io() )
            .flatMap { fInitialized ->
                when( fInitialized ) {
                    true -> getSummonerModel( Orianna.summonerWithPuuid( puuid ).get().also { it.load() } )
                    else -> throw RepositoryNotInitializedException()
                }
            }
    }

    @WorkerThread
    fun getActiveSummoner(): Observable<SummonerModel> {

        return initialized.observeOn( Schedulers.io() )
            .flatMap { fInitialized ->
                when( fInitialized ) {
                    true -> {
                        val curSumId = database.globalStateDAO().getAll()[ 0 ].curSummonerId

                        return@flatMap if( curSumId >= 0 ) {

                            val se = database.summonerDAO().getById( curSumId )
                            findSummonerByPuuid( se.puuid )
                        } else Observable.empty()
                    }
                    else -> throw RepositoryNotInitializedException()
                }
            }
    }

    @SuppressLint( "CheckResult")
    @WorkerThread
    fun addSummoner( summonerModel: SummonerModel, setActive: Boolean = false ) {
        initialized.observeOn( Schedulers.io() )
            .subscribe { fInitialized ->
                when( fInitialized ) {
                    true -> {
                        val summonerEntity = SummonerEntity( summonerModel.puuid )
                        val sumId = database.summonerDAO().insert( summonerEntity )
                        if( setActive ) {
                            val globalState = database.globalStateDAO().getAll()[ 0 ]
                            globalState.curSummonerId = sumId
                            database.globalStateDAO().update( globalState )
                        }
                    }
                    else -> throw RepositoryNotInitializedException()
                }
            }
    }

    fun getChampionMasteryModel( championMastery: ChampionMastery ) = ensureInitializedDoOnIO { ChampionMasteryModel( this, championMastery ) }

    fun getChampionModel( champion: Champion ) = ensureInitializedDoOnIO { champions[ champion.id ]?: ChampionModel( this, champion ).also { champions[ it.id ] = it } }

    fun getMatchHistoryModel( matchHistory: MatchHistory ) = ensureInitializedDoOnIO { MatchHistoryModel( this, matchHistory ) }

    fun getMatchModel( match: Match ) = ensureInitializedDoOnIO { MatchModel( this, match ) }

    fun getParticipantModel( teamModel: TeamModel, participant: Participant ) = ensureInitializedDoOnIO { ParticipantModel( this, teamModel, participant ) }

    fun getSummonerModel( summoner: Summoner ) = ensureInitializedDoOnIO { summoners[ summoner.puuid ]?: SummonerModel( this, summoner ).also { summoners[ it.puuid ] = it } }

    fun getTeamModel( team: Team ) = ensureInitializedDoOnIO { TeamModel( this, team ) }

    fun getItemModel( item: Item ) = ensureInitializedDoOnIO { ItemModel( this, item ) }

    fun getSummonerSpell( l: () -> SummonerSpell ) = ensureInitializedDoOnIO {
        val spell = l();
        summonerSpells[ spell.id ]?: SummonerSpellModel( this, spell ).also { summonerSpells[ spell.id ] = it }
    }

    fun getRuneStats( runeStats: RuneStats ) = ensureInitializedDoOnIO { RuneStatsModel( this, runeStats ) }

    fun getRune( reforgedRune: ReforgedRune ) = ensureInitializedDoOnIO { runes[ reforgedRune.id ]?: RuneModel( this, reforgedRune ).also { runes[ reforgedRune.id ] = it } }

    companion object {
        fun getRunePath( pathId: Int ) = RunePathModel.byId[ pathId ]?: throw RuntimeException( "Incorrect rune pathj ID: $pathId" )
        fun getGameType( gameType: GameType? = null, queue: Queue? = null, gameMode: GameMode? = null, gameMap: GameMap? = null ) =
            GameTypeModel.by( gameType, queue, gameMode, gameMap )
    }

    private fun<T> ensureInitializedDoOnIO( l: () -> T ): Observable<T> {
        return initialized.observeOn( Schedulers.io() ).map { fInitialized ->
            when( fInitialized ) {
                true -> l()!!
                else -> throw RepositoryNotInitializedException()
            }
        }
    }
}
