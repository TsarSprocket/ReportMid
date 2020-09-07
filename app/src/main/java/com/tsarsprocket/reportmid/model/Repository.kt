package com.tsarsprocket.reportmid.model

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.room.Room
import com.merakianalytics.orianna.Orianna
import com.merakianalytics.orianna.types.common.*
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
import com.tsarsprocket.reportmid.room.GlobalStateEntity
import com.tsarsprocket.reportmid.room.MainStorage
import com.tsarsprocket.reportmid.room.MyAccountEntity
import com.tsarsprocket.reportmid.room.SummonerEntity
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
class Repository @Inject constructor( val context: Context ) {

    val initialized = ReplaySubject.createWithSize<Boolean>( 1 )

    lateinit var database: MainStorage

    val summoners = ConcurrentHashMap<String,SummonerModel>()
    val champions = ConcurrentHashMap<Int,ChampionModel>()
    val summonerSpells = ConcurrentHashMap<Int,SummonerSpellModel>()
    val runes = ConcurrentHashMap<Int,RuneModel>()

    init {

        Observable.fromCallable {
            try {
                Log.d( Repository::class.simpleName, "Initializing..." )

                database = Room.databaseBuilder( context.applicationContext, MainStorage::class.java, "database" ).build()

                val stateList = database.globalStateDAO().getAll()

                if( stateList.isEmpty() ) {

                    val accs = database.myAccountDAO().getAll()

                    database.globalStateDAO().insert( GlobalStateEntity( accs.firstOrNull()?.id ) )
                }

//                  Orianna.loadConfiguration( CharSource.wrap( loadOriannaConfigToString() ) )
                Orianna.setRiotAPIKey( loadRawResourceAsText(R.raw.riot_api_key) )
                Orianna.setDefaultRegion( Region.RUSSIA )

                Log.d( Repository::class.simpleName, "Done initialize" )

                return@fromCallable true
            } catch ( ex: Exception ) {

                return@fromCallable false
            }
        }.subscribeOn( Schedulers.io() ).subscribe( initialized )
    }

    private fun loadRawResourceAsText( resId: Int ) = InputStreamReader( context.resources.openRawResource( resId ) ).readText()

    @WorkerThread
    fun findSummonerForName( summonerName: String, regionModel: RegionModel ): Observable<SummonerModel> {

        return initialized.observeOn( Schedulers.io() )
            .flatMap { fInitialized ->
                when( fInitialized ) {
                    true -> getSummonerModel{ Orianna.summonerNamed( summonerName ).withRegion( regionModel.shadowRegion ).get() }
                    else -> throw RepositoryNotInitializedException()
                }
            }
    }

    @WorkerThread
    fun findSummonerByPuuid( puuid: String? ) = getSummonerModel{ Orianna.summonerWithPuuid( puuid ).get().also { it.load() } }

    @WorkerThread
    fun getActiveSummonerPUUID() = initialized.observeOn( Schedulers.io() )
        .flatMap { fInitialized ->
            when( fInitialized ) {
                true -> {
                    val curAccId = database.globalStateDAO().getAll().first().curMyAccountId

                    if( curAccId != null ) {
                        val curSummonerId = database.myAccountDAO().getById( curAccId ).summonerId
                        Observable.just( database.summonerDAO().getById( curSummonerId ).puuid )
                    } else  Observable.empty()
                }
                else -> throw RepositoryNotInitializedException()
            }
        }

    @WorkerThread
    fun getActiveSummoner() = getActiveSummonerPUUID().flatMap { puuid -> findSummonerByPuuid( puuid ) }

    @SuppressLint( "CheckResult")
    @WorkerThread
    fun addSummoner( summonerModel: SummonerModel, setActive: Boolean = false ) {
        initialized.observeOn( Schedulers.io() )
            .subscribe { fInitialized ->
                when( fInitialized ) {
                    true -> {
                        val regionEntity = database.regionDAO().getByTag( summonerModel.region.tag )
                        val summonerEntity = SummonerEntity( summonerModel.puuid, regionEntity.id )
                        val sumId = database.summonerDAO().insert( summonerEntity )
                        val myAccountEntity = MyAccountEntity( sumId )
                        val accId = database.myAccountDAO().insert( myAccountEntity )
                        if( setActive ) {
                            val globalState = database.globalStateDAO().getAll()[ 0 ]
                            globalState.curMyAccountId = accId
                            database.globalStateDAO().update( globalState )
                        }
                    }
                    else -> throw RepositoryNotInitializedException()
                }
            }
    }

    fun getChampionMasteryModel( championMastery: ChampionMastery ) = ensureInitializedDoOnIO { ChampionMasteryModel( this, championMastery ) }

    fun getChampionModel( lmdChampion: () -> Champion ) = ensureInitializedDoOnIO {
        val champion = lmdChampion()
        champions[ champion.id ]?: ChampionModel( this, champion ).also { champions[ it.id ] = it }
    }

    fun getMatchHistoryModel( matchHistory: MatchHistory ) = ensureInitializedDoOnIO { MatchHistoryModel( this, matchHistory ) }

    fun getMatchModel( match: Match ) = ensureInitializedDoOnIO { MatchModel( this, match ) }

    fun getParticipantModel( teamModel: TeamModel, participant: Participant ) = ensureInitializedDoOnIO { ParticipantModel( this, teamModel, participant ) }

    fun getSummonerModel( lmdSummoner: () -> Summoner ) = ensureInitializedDoOnIO {
        val summoner = lmdSummoner()
        summoners[ summoner.puuid ]?: SummonerModel( this, summoner ).also { summoners[ it.puuid ] = it }
    }

    fun getTeamModel( team: Team ) = ensureInitializedDoOnIO { TeamModel( this, team ) }

    fun getItemModel( item: Item ) = ensureInitializedDoOnIO { ItemModel( this, item ) }

    fun getSummonerSpell( lmdSummonerSpell: () -> SummonerSpell ) = ensureInitializedDoOnIO {
        val spell = lmdSummonerSpell()
        summonerSpells[ spell.id ]?: SummonerSpellModel( this, spell ).also { summonerSpells[ spell.id ] = it }
    }

    fun getRuneStats( runeStats: RuneStats ) = ensureInitializedDoOnIO { RuneStatsModel( this, runeStats ) }

    fun getRune( lmdReforgedRune: () -> ReforgedRune ) = ensureInitializedDoOnIO {
        val reforgedRune = lmdReforgedRune()
        runes[ reforgedRune.id ]?: RuneModel( this, reforgedRune ).also { runes[ reforgedRune.id ] = it }
    }

    fun getCurrentMatch( lmdCurrentMatch: () -> CurrentMatch ) = ensureInitializedDoOnIO { CurrentMatchModel( this, lmdCurrentMatch() ) }

    fun getCurrentMatchTeam( lmdCurrentMatchTeam: () -> CurrentMatchTeam ) = ensureInitializedDoOnIO { CurrentMatchTeamModel( this, lmdCurrentMatchTeam() ) }

    fun getPlayer( lmdPlayer: () -> Player ) = ensureInitializedDoOnIO { PlayerModel( this, lmdPlayer() ) }

    fun getPlayerRunes( lmdRunes: () -> Runes ) = ensureInitializedDoOnIO { PlayerRunesModel( this, lmdRunes() ) }

    fun getLeaguePosition( lmdLeagueEntry: () -> LeagueEntry? ) = ensureInitializedDoOnIO { lmdLeagueEntry()?.let { LeaguePositionModel( this, it ) } }

    companion object {
        val allRegions = RegionModel.values()
        fun getRegion( region: Region ) = RegionModel.byShadowRegion[ region ]?: throw RuntimeException( "No such region: ${region.tag}" )
        fun getRunePath( pathId: Maybe<Int> ) = if( pathId.isEmpty.blockingGet() ) Maybe.empty() else Maybe.just( RunePathModel.byId[ pathId.blockingGet() ]?: throw IncorrectRunePathIdException( pathId.blockingGet() ) )
        fun getGameType( gameType: GameType? = null, queue: Queue? = null, gameMode: GameMode? = null, gameMap: GameMap? = null ) =
            GameTypeModel.by( gameType, queue, gameMode, gameMap )
        fun getSide( side: Side ) = SideModel.fromExternal( side )
        fun getQueue( queue: Queue ) = QueueModel.values().find { it.shadowQueue.id == queue.id }?: throw RuntimeException( "Queue $queue is not defined in the model" )
        fun getTier( tier: Tier ) = TierModel.values().find { it.shadowTier == tier }?: throw RuntimeException( "Tier $tier is not mapped" )
        fun getDivision( division: Division ) = DivisionModel.values().find { it.shadowDivision == division }?: throw RuntimeException( "Division $division is not mapped" )

        @WorkerThread
        fun findParticipant( match: MatchModel, summoner: SummonerModel ): ParticipantModel {
            val blueTeam = match.blueTeam.blockingSingle()
            val redTeam = match.redTeam.blockingSingle()

            val firstInBlue = blueTeam.participants.first().blockingSingle()
            if( firstInBlue.summoner.blockingSingle().puuid == summoner.puuid ) return firstInBlue

            val firstInRed = redTeam.participants.first().blockingSingle()
            if( firstInRed.summoner.blockingSingle().puuid == summoner.puuid ) return firstInRed

            return (
                    blueTeam.participants.subList( 1, blueTeam.participants.size )
                        .union( redTeam.participants.subList( 1, redTeam.participants.size ) )
                        .find { it.blockingSingle().summoner.blockingSingle().puuid == summoner.puuid }
                        ?: throw RuntimeException("Summoner ${summoner.name} is not found in match ${match.id}")
                    ).blockingSingle()
        }

        @WorkerThread
        fun getItemIcons( participant: ParticipantModel ): Array<Bitmap> {
            val items = participant.items.blockingSingle()
            return Array( items.size ) { i -> items[ i ].blockingSingle().bitmap.blockingSingle() }
        }

        @WorkerThread
        fun getTeamIcons( team: TeamModel, resizeMatrix: Matrix) = Array( team.participants.size ) { i ->
            val bm = team.participants[i].blockingSingle().champion.blockingSingle().bitmap.blockingSingle()
            Bitmap.createBitmap( bm,0, 0, bm.width, bm.height, resizeMatrix, false )
        }

        @WorkerThread
        fun calculateTeamKDA( asParticipant: ParticipantModel ): Triple<Int,Int,Int> {
            var teamKills = 0
            var teamDeaths = 0
            var teamAssists = 0
            asParticipant.team.participants.forEach {
                val participant = it.blockingSingle()
                teamKills += participant.kills
                teamDeaths += participant.deaths
                teamAssists += participant.assists
            }
            return Triple( teamKills, teamDeaths, teamAssists )
        }
    }

    /*  Tools ****************************************************************/

    private fun<T> ensureInitializedDoOnIO( l: () -> T? ) = ReplaySubject.createWithSize<T>( 1 ).also { subject ->
        initialized.observeOn( Schedulers.io() ).flatMap { fInitialized ->
            when( fInitialized ) {
                true -> {
                    val v = l()
                    if( v != null ) Observable.just( v ) else Observable.empty()
                }
                else -> Observable.error( RepositoryNotInitializedException() )
            }
        }.subscribe( subject )
    }
}
