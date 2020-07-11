package com.tsarsprocket.reportmid

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tsarsprocket.reportmid.model.RegionModel
import com.tsarsprocket.reportmid.model.Repository
import com.tsarsprocket.reportmid.model.SummonerModel
import com.tsarsprocket.reportmid.presentation.MatchResultPreviewData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

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

    val allDisposables = CompositeDisposable()

    init {
        repository.getActiveSummoner()
            .observeOn( AndroidSchedulers.mainThread() )
            .subscribe( object: DisposableObserver<SummonerModel>() {
                override fun onComplete() {
                    if( activeSummonerModel.value == null ) state.value = Status.UNVERIFIED
                }
                override fun onNext( summonerModel: SummonerModel ) {
                    activeSummonerModel.value = summonerModel
                    loadMasteries()
                    state.value = Status.VERIFIED
                }
                override fun onError( e: Throwable ) {
                    Log.d( LandingViewModel::class.simpleName, "Cannot initialize LandingViewModel", e )
                }
            }.also { allDisposables.add( it ) } )
    }

    override fun onCleared() {
        super.onCleared()

        allDisposables.dispose()
    }

    fun selectRegionByOrderNo(orderNo: Int ) {

        selectedRegion.value = if( orderNo >= 0 && orderNo < allRegions.size ) allRegions[ orderNo ] else null
    }

    fun validateInitial( action: ( fResult: Boolean ) -> Unit ) {

        val reg = enumValues<RegionModel>().find { it == selectedRegion.value } ?: throw RuntimeException( "Incorrect region code \'${selectedRegion}\'" )

        allDisposables.add(
            repository.findSummonerForName( activeSummonerName.value?: "", reg )
                .observeOn( Schedulers.io() )
                .doOnNext { summonerModel ->
                    repository.addSummoner( summonerModel, true )
                }
                .observeOn( AndroidSchedulers.mainThread() )
                .subscribe { summonerModel ->
                    activeSummonerModel.value = summonerModel
                    state.value = if( summonerModel != null ) Status.VERIFIED else Status.UNVERIFIED
                    action( summonerModel != null )
                }
        )
    }

    @SuppressLint("CheckResult")
    private fun loadMasteries() {
        for( i in 0 until TOP_MASTERIES_NUM ) {
            activeSummonerModel.value!!.masteries
                .flatMap { masteryList -> masteryList[ i ] }
                .observeOn( AndroidSchedulers.mainThread() )
                .subscribe() { masteryModel ->
                    masteryModel.champion
                        .observeOn( AndroidSchedulers.mainThread() )
                        .subscribe { champ ->
                            allDisposables.add(
                                champ.bitmap.subscribe { bitmap ->
                                    championImages[ i ].postValue( bitmap )
                                }
                            )
                        }
                }
        }
    }

    fun fetchMatchPreviewInfo(
        position: Int
    ): Observable<MatchResultPreviewData> {
        val summoner = activeSummonerModel.value?:return Observable.empty()

        return summoner.matchHistory
            .observeOn( Schedulers.io() )
            .flatMap { matchHistoryModel ->
                matchHistoryModel.getMatch(position)
            }
            .map { match ->
                val blueTeam = match.blueTeam.blockingSingle()
                val redTeam = match.redTeam.blockingSingle()

                val asParticipant =  (
                        blueTeam.participants
                            .union( redTeam.participants )
                            .find { it.blockingSingle().summoner.blockingSingle().puuid == summoner.puuid }
                            ?: throw RuntimeException( "Summoner ${summoner.name} is not found in match ${match.id}" )
                    ).blockingSingle()

                var teamKills = 0
                var teamDeaths = 0
                var teamAssists = 0
                asParticipant.team.participants.forEach() {
                    val participant = it.blockingSingle()
                    teamKills += participant.kills
                    teamDeaths += participant.deaths
                    teamAssists += participant.assists
                }

                val resizeMatrix = Matrix().apply { postScale(0.5f, 0.5f) }

                MatchResultPreviewData(
                    asParticipant.champion.blockingSingle().bitmap.blockingSingle(),
                    asParticipant.kills,
                    asParticipant.deaths,
                    asParticipant.assists,
                    teamKills,
                    teamDeaths,
                    teamAssists,
                    asParticipant.isWinner,
                    Array( blueTeam.participants.size ) { i ->
                        val bm = blueTeam.participants[i].blockingSingle().champion.blockingSingle().bitmap.blockingSingle()
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
                    Array( redTeam.participants.size ) { i ->
                        val bm = redTeam.participants[i].blockingSingle().champion.blockingSingle().bitmap.blockingSingle()
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
            }
    }

}