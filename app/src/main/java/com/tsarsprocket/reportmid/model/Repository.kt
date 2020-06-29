package com.tsarsprocket.reportmid.model

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.room.Room
import com.merakianalytics.orianna.Orianna
import com.merakianalytics.orianna.types.core.championmastery.ChampionMastery
import com.merakianalytics.orianna.types.core.match.Match
import com.merakianalytics.orianna.types.core.match.MatchHistory
import com.merakianalytics.orianna.types.core.match.Participant
import com.merakianalytics.orianna.types.core.match.Team
import com.merakianalytics.orianna.types.core.staticdata.Champion
import com.merakianalytics.orianna.types.core.summoner.Summoner
import com.tsarsprocket.reportmid.R
import com.tsarsprocket.reportmid.room.GlobalStateEntity
import com.tsarsprocket.reportmid.room.MainStorage
import com.tsarsprocket.reportmid.room.SummonerEntity
import kotlinx.coroutines.*
import java.io.InputStreamReader
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor( val context: Context ) {

    lateinit var fInitialized: Deferred<Boolean>
    lateinit var database: MainStorage

    val summoners = HashMap<String,SummonerModel>()
    val champions = HashMap<Int,ChampionModel>()

    init {
        GlobalScope.launch {

            fInitialized = async( Dispatchers.IO ) {

                try {
                    database = Room.databaseBuilder( context.applicationContext, MainStorage::class.java, "database" ).build()

                    val stateList = database.globalStateDAO().getAll()

                    if( stateList.isEmpty() ) {

                        val sums = database.summonerDAO().getAll()

                        database.globalStateDAO().insert( GlobalStateEntity( if( sums.isNotEmpty() ) sums[ 0 ].id else -1 ) )
                    }

//                  Orianna.loadConfiguration( CharSource.wrap( loadOriannaConfigToString() ) )
                    Orianna.setRiotAPIKey( loadRawResourceAsText(R.raw.riot_api_key) )
                    Orianna.setDefaultRegion(com.merakianalytics.orianna.types.common.Region.RUSSIA)

                    return@async true
                } catch ( ex: Exception ) {

                    return@async false
                }
            }
        }
    }

    private fun loadRawResourceAsText( resId: Int ) = InputStreamReader( context.resources.openRawResource( resId ) ).readText()

    val allRegions = RegionModel.values()

    @WorkerThread
    suspend fun findSummonerForName(summonerName: String, regionModel: RegionModel ): SummonerModel {

        fInitialized.await()

        val sum = Orianna.summonerNamed(summonerName).withRegion(regionModel.shadowRegion).get()

        return getSummonerModel( sum )
    }

    @WorkerThread
    suspend fun findSummonerByPuuid( puuid: String? ): SummonerModel? {

        fInitialized.await()

        return getSummonerModel( Orianna.summonerWithPuuid( puuid ).get().also{ it.load() } )
    }

    @WorkerThread
    suspend fun getActiveSummoner(): SummonerModel? {

        fInitialized.await()

        val curSumId = database.globalStateDAO().getAll()[ 0 ].curSummonerId

        return if( curSumId >= 0 ) {

            val se = database.summonerDAO().getById( curSumId )
            findSummonerByPuuid( se.puuid )
        } else null
    }

    @WorkerThread
    suspend fun addSummoner( summonerModel: SummonerModel, setActive: Boolean = false ) {

        fInitialized.await()

        val summonerEntity = SummonerEntity( summonerModel.puuid )

        val sumId = database.summonerDAO().insert( summonerEntity )

        if( setActive ) {

            val globalState = database.globalStateDAO().getAll()[ 0 ]

            globalState.curSummonerId = sumId

            database.globalStateDAO().update( globalState )
        }
    }

    fun getChampionMasteryModel( championMastery: ChampionMastery ): ChampionMasteryModel = ChampionMasteryModel( this, championMastery )

    fun getChampionModel( champion: Champion ): ChampionModel = champions[ champion.id ]?: ChampionModel( this, champion ).also { champions[ it.id ] = it }

    fun getMatchHistoryModel( matchHistory: MatchHistory ): MatchHistoryModel = MatchHistoryModel( this, matchHistory )

    fun getMatchModel( match: Match ): MatchModel = MatchModel( this, match )

    fun getParticipantModel( teamModel: TeamModel, participant: Participant ): ParticipantModel = ParticipantModel( this, teamModel, participant )

    fun getSummonerModel( summoner: Summoner ): SummonerModel = summoners[ summoner.puuid ]?: SummonerModel( this, summoner ).also { summoners[ it.puuid ] = it }

    fun getTeamModel( team: Team ): TeamModel = TeamModel( this, team )
}
