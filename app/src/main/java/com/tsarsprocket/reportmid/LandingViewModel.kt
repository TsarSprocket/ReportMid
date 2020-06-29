package com.tsarsprocket.reportmid

import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tsarsprocket.reportmid.presentation.MatchResultPreviewData
import com.tsarsprocket.reportmid.model.RegionModel
import com.tsarsprocket.reportmid.model.Repository
import com.tsarsprocket.reportmid.model.SummonerModel
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.Exception

const val TOP_MASTERIES_NUM = 5

class LandingViewModel @Inject constructor( private val repository: Repository) : ViewModel() {

    enum class Status { LOADING, UNVERIFIED, VERIFIED }

    val allRegions = repository.allRegions
    val regionTitles
        get() = allRegions.map { it.title }

    val selectedRegion = MutableLiveData<RegionModel>()

    var activeSummonerName = MutableLiveData<String>( "" )

    var activeSummonerModel = MutableLiveData<SummonerModel>()

    val state = MutableLiveData<Status>( Status.LOADING )

    val championImages = Array( TOP_MASTERIES_NUM ) { MutableLiveData<Bitmap>() }

    init {
        viewModelScope.launch {
            val futureSum = async( Dispatchers.IO ) { repository.getActiveSummoner() }
            activeSummonerModel.value = futureSum.await()
            loadMasteries()
            if( activeSummonerModel.value != null ) state.value = Status.VERIFIED
        }
    }

    fun selectRegionByOrderNo( orderNo: Int ) {

        selectedRegion.value = if( orderNo >= 0 && orderNo < allRegions.size ) allRegions[ orderNo ] else null
    }

    fun validateInitial( action: ( fResult: Boolean ) -> Unit ) {

        val reg = enumValues<RegionModel>().find { it == selectedRegion.value } ?: throw RuntimeException( "Incorrect region code \'${selectedRegion}\'" )

        val job = viewModelScope.launch {

            val futureSummoner = async( Dispatchers.IO ) {
                repository.findSummonerForName( activeSummonerName.value?: "", reg ).also { repository.addSummoner( it, true ) }
            }

            activeSummonerModel.value = futureSummoner.await()

            state.value = if( activeSummonerModel.value != null ) Status.VERIFIED else Status.UNVERIFIED

            action( activeSummonerModel.value != null )
        }
    }

    private fun loadMasteries() {
        for( i in 0 until TOP_MASTERIES_NUM ) {
            activeSummonerModel.value!!.masteries[ i ].loadAsync()
                .observeOn( AndroidSchedulers.mainThread() )
                .subscribe() { m ->
                    championImages[ i ].value = m.champion.bitmap
                }
        }
    }

    fun fetchMatchPreviewInfo( position: Int, processData: (MatchResultPreviewData) -> Unit ) {

        viewModelScope.launch( Dispatchers.IO ) {

            val summoner = activeSummonerModel.value?:return@launch
            try {
                Log.d( LandingViewModel::class.simpleName, "Load match info for position $position" )
                val match = summoner.matchHistory.getMatch( position )

                val asParticipant = match.blueTeam.participants
                    .union( match.redTeam.participants )
                    .find { it.summoner.puuid == summoner.puuid }?: throw RuntimeException( "Summoner ${summoner.name} is not found in match ${match.id}" )
                var teamKills = 0
                var teamDeaths = 0
                var teamAssists = 0
                asParticipant.team.participants.forEach() {
                    teamKills += it.kills
                    teamDeaths += it.deaths
                    teamAssists += it.assists
                }
                val resizeMatrix = Matrix().apply { postScale(0.5f, 0.5f) }
                val result =
                    MatchResultPreviewData(
                        asParticipant.champion.bitmap,
                        asParticipant.kills,
                        asParticipant.deaths,
                        asParticipant.assists,
                        teamKills,
                        teamDeaths,
                        teamAssists,
                        asParticipant.isWinner,
                        Array(match.blueTeam.participants.size) { i ->
                            val bm = match.blueTeam.participants[i].champion.bitmap
                            return@Array Bitmap.createBitmap(
                                bm,
                                0,
                                0,
                                bm.width,
                                bm.height,
                                resizeMatrix,
                                false
                            )
                        },
                        Array(match.redTeam.participants.size) { i ->
                            val bm = match.redTeam.participants[i].champion.bitmap
                            return@Array Bitmap.createBitmap(
                                bm,
                                0,
                                0,
                                bm.width,
                                bm.height,
                                resizeMatrix,
                                false
                            )
                        }
                    )

                launch( Dispatchers.Main ) {
                    processData( result )
                }
            } catch ( ex: Exception ) {
                Log.e( LandingViewModel::class.simpleName, ex.localizedMessage, ex )
            }
        }
    }

}