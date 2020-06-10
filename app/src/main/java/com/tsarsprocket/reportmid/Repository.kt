package com.tsarsprocket.reportmid

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.room.Room
import com.merakianalytics.orianna.Orianna
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
                    Orianna.setRiotAPIKey("RGAPI-e93d4bdd-19d4-4529-ac6d-e5b0442e7912")
                    Orianna.setDefaultRegion(com.merakianalytics.orianna.types.common.Region.RUSSIA)

                    return@async true
                } catch ( ex: Exception ) {

                    return@async false
                }
            }
        }
    }

//    private fun loadOriannaConfigToString() = InputStreamReader( context.resources.openRawResource( R.raw.orianna_config ) ).readText()

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
}