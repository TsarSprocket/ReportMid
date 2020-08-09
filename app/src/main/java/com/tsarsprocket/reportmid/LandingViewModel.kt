package com.tsarsprocket.reportmid

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tsarsprocket.reportmid.model.*
import com.tsarsprocket.reportmid.presentation.MasteryLive
import com.tsarsprocket.reportmid.presentation.MatchResultPreviewData
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

const val TOP_MASTERIES_NUM = 5

class LandingViewModel @Inject constructor( private val repository: Repository ) : ViewModel() {

    enum class Status { LOADING, UNVERIFIED, VERIFIED }

    val allRegions = repository.allRegions
    val regionTitles
        get() = allRegions.map { it.title }

    val selectedRegion = MutableLiveData<RegionModel>()

    var activeSummonerName = MutableLiveData<String>( "" )

    var activeSummonerModel = MutableLiveData<SummonerModel>()

    val state = MutableLiveData<Status>( Status.LOADING )

    val masteries = Array( TOP_MASTERIES_NUM ) { MasteryLive() }
//    val championImages = Array( TOP_MASTERIES_NUM ) { MutableLiveData<Maybe<Bitmap>>() }

    val allDisposables = CompositeDisposable()
    val summonerDisposables = CompositeDisposable()

    init {
        allDisposables.add( repository.getActiveSummoner()
            .observeOn( AndroidSchedulers.mainThread() )
            .doOnNext { summonerModel ->
                activeSummonerModel.value = summonerModel
                state.value = Status.VERIFIED
            }
            .doOnComplete { if( activeSummonerModel.value == null ) state.value = Status.UNVERIFIED }
            .doOnError { e -> Log.d( LandingViewModel::class.simpleName, "Cannot initialize LandingViewModel", e ) }
            .subscribe() )
        observeMasteries()
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
    private fun observeMasteries() {
        activeSummonerModel.observeForever { summoner ->
            summonerDisposables.clear()
            summonerDisposables.add( summoner.masteries.flatMapIterable { arrMasteries ->
                val extraSize = TOP_MASTERIES_NUM - arrMasteries.size
                val indexed: List<IndexedValue<Maybe<Observable<ChampionMasteryModel>>>> = arrMasteries.take( TOP_MASTERIES_NUM ).withIndex().map { IndexedValue( it.index, Maybe.just( it.value ) ) }
                if( extraSize > 0 ) indexed + List( extraSize ) { i -> IndexedValue( indexed.size + i, Maybe.empty<Observable<ChampionMasteryModel>>() ) }
                else indexed
            }
                .flatMap { indexedObservableMastery ->
                    if( indexedObservableMastery.value.isEmpty.blockingGet() ) Observable.just( Pair( indexedObservableMastery.index, Maybe.empty() ) )
                    else indexedObservableMastery.value.blockingGet().map{ mastery ->
                            masteries[ indexedObservableMastery.index ].skillsLive.postValue( MasteryLive.Skills( level = mastery.level, points = mastery.points ) )
                            return@map Pair( indexedObservableMastery.index, Maybe.just( mastery ) )
                        }
                }
                .flatMap { indexedMastery ->
                    if( indexedMastery.second.isEmpty.blockingGet() ) Observable.just( Pair( indexedMastery.first, Maybe.empty() ) )
                    else indexedMastery.second.blockingGet().champion.map { champion ->
                        masteries[ indexedMastery.first ].champNameLive.postValue( champion.name )
                        return@map Pair( indexedMastery.first, Maybe.just( champion ) )
                    }
                }
                .subscribe { indexedChamp ->
                    if( indexedChamp.second.isEmpty.blockingGet() ) masteries[ indexedChamp.first ].shownLive.postValue( false )
                    else indexedChamp.second.blockingGet().bitmap.subscribe{ bitmap ->
                        with( masteries[ indexedChamp.first ] ) {
                            bitmapLive.postValue( bitmap )
                            shownLive.postValue( true )
                        }
                    }
                }
            )
        }


/*
        val observableMasteries = activeSummonerModel.value?.masteries
        if( observableMasteries != null ) {
            allDisposables.add( observableMasteries.subscribe { masteries ->
                for( i in 0 until if ( masteries.size < TOP_MASTERIES_NUM ) masteries.size else TOP_MASTERIES_NUM ) {
                    masteries[i].observeOn(AndroidSchedulers.mainThread())
                        .flatMap { mastery -> mastery.champion }
                        .flatMap { champ -> champ.bitmap }
                        .observeOn( AndroidSchedulers.mainThread() )
                        .subscribe { bitmap ->
                            championImages[i].value = bitmap
                        }

                }
            } )
        }
*/

/*
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
*/
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

                val maybeSecondaryPath = asParticipant.secondaryRunePath.blockingSingle()

                MatchResultPreviewData(
                    asChampion.bitmap.blockingSingle(),
                    asParticipant.kills,
                    asParticipant.deaths,
                    asParticipant.assists,
                    asParticipant.isWinner,
                    match.remake,
                    asParticipant.creepScore,
                    match.gameType.titleResId,
                    runeModels.find { it.slot == 0 }?.iconResId,
                    if( maybeSecondaryPath.isEmpty.blockingGet() ) null else maybeSecondaryPath.blockingGet().iconResId,
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