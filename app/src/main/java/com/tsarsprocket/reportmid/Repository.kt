package com.tsarsprocket.reportmid

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.annotation.WorkerThread
import androidx.room.Room
import com.merakianalytics.orianna.Orianna
import com.tsarsprocket.reportmid.model.MatchModel
import com.tsarsprocket.reportmid.model.MatchResultPreviewData
import com.tsarsprocket.reportmid.model.RegionModel
import com.tsarsprocket.reportmid.model.SummonerModel
import com.tsarsprocket.reportmid.room.GlobalStateEntity
import com.tsarsprocket.reportmid.room.MainStorage
import com.tsarsprocket.reportmid.room.SummonerEntity
import kotlinx.coroutines.*
import java.io.InputStreamReader
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor( private val context: Context ) {

    lateinit var fInitialized: Deferred<Boolean>
    lateinit var database: MainStorage

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
                    Orianna.setRiotAPIKey( loadRawResourceAsText( R.raw.riot_api_key ) )
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

        return SummonerModel( sum )
    }

    @WorkerThread
    suspend fun findSummonerByPuuid( puuid: String? ): SummonerModel? {

        fInitialized.await()

        return SummonerModel( Orianna.summonerWithPuuid( puuid ).get().also{ it.load() } )
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

    @WorkerThread
    fun getMatchResultPreviewData( match: MatchModel, summoner: SummonerModel ): MatchResultPreviewData {

        val asParticipant = match.shadowMatch.blueTeam.participants
            .union( match.shadowMatch.redTeam.participants )
            .find { it.summoner.puuid == summoner.puuid }?: throw RuntimeException( "Summoner ${summoner.name} is not found in match ${match.shadowMatch.id}" )

        val participantStats = asParticipant.stats

        var teamKills = 0
        var teamDeaths = 0
        var teamAssists = 0

        asParticipant.team.participants.forEach() {
            teamKills += it.stats.kills
            teamDeaths += it.stats.deaths
            teamAssists += it.stats.assists
        }

        val resizeMatrix = Matrix().apply { postScale(0.5f, 0.5f) }

        return MatchResultPreviewData(
            asParticipant.champion.image.get(),
            participantStats.kills,
            participantStats.deaths,
            participantStats.assists,
            teamKills,
            teamDeaths,
            teamAssists,
            participantStats.isWinner,
            Array<Bitmap>( match.shadowMatch.blueTeam.participants.size ) { i ->
                val bm = match.shadowMatch.blueTeam.participants[i].champion.image.get()
                return@Array Bitmap.createBitmap( bm, 0, 0, bm.width, bm.height, resizeMatrix, false )
            },
            Array<Bitmap>( match.shadowMatch.redTeam.participants.size ) { i ->
                val bm = match.shadowMatch.redTeam.participants[i].champion.image.get()
                return@Array Bitmap.createBitmap( bm, 0, 0, bm.width, bm.height, resizeMatrix, false )
            }
        )
    }
}