package com.tsarsprocket.reportmid

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tsarsprocket.reportmid.model.*
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
                val asParticipant = findParticipant( match, summoner )
                val asChampion = asParticipant.champion.blockingSingle()

//                val resizeMatrix = Matrix().apply { postScale( 0.5f, 0.5f ) }

                val runeModels = asParticipant.runeStats.blockingSingle().map { o -> o.blockingSingle().rune.blockingSingle() }

                // The two exceptions below should never be thrown
                val primaryRune = runeModels.find { it.slot == 0 }?: throw RuntimeException( "Did not find rune with slot 0 in ${asChampion.name}" )

                MatchResultPreviewData(
                    asChampion.bitmap.blockingSingle(),
                    asParticipant.kills,
                    asParticipant.deaths,
                    asParticipant.assists,
                    asParticipant.isWinner,
                    match.remake,
                    asParticipant.creepScore,
                    match.gameType.titleResId,
                    primaryRune.iconResId,
                    asParticipant.secondaryRunePath.blockingSingle()!!.iconResId,
                    asParticipant.summonerSpellD.blockingSingle().icon.blockingSingle(),
                    asParticipant.summonerSpellF.blockingSingle().icon.blockingSingle(),
                    getItemIcons( asParticipant )
                )
            }
    }

    @WorkerThread
    private fun getItemIcons( participant: ParticipantModel ): Array<Bitmap> {
        val items = participant.items.blockingSingle()
        return Array( items.size ) { i -> items[ i ].blockingSingle().bitmap.blockingSingle() }
    }

    @WorkerThread
    private fun findParticipant( match: MatchModel, summoner: SummonerModel ): ParticipantModel {

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
    private fun getTeamIcons( team: TeamModel, resizeMatrix: Matrix ) = Array( team.participants.size ) { i ->
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