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

    val allDisposables = CompositeDisposable()
    val summonerDisposables = CompositeDisposable()

    init {
        allDisposables.add( repository.getActiveSummoner()
            .observeOn( AndroidSchedulers.mainThread() )
            .doOnNext { summonerModel ->
                activeSummonerModel.value = summonerModel
                state.value = Status.VERIFIED
            }
            .doOnComplete {
                if( activeSummonerModel.value == null ) state.value = Status.UNVERIFIED
            }
            .doOnError { e -> Log.d( LandingViewModel::class.simpleName, "Cannot initialize LandingViewModel", e ) }
            .subscribe() )
        observeMasteries()
    }

    override fun onCleared() {
        allDisposables.dispose()
        super.onCleared()
    }

    fun selectRegionByOrderNo( orderNo: Int ) {
        selectedRegion.value = if( orderNo >= 0 && orderNo < allRegions.size ) allRegions[ orderNo ] else null
    }

    fun validateInitial( action: ( fResult: Boolean ) -> Unit ) {
        val reg = enumValues<RegionModel>().find { it == selectedRegion.value } ?: throw RuntimeException( "Incorrect region code \'${selectedRegion}\'" )
        allDisposables.add(
            repository.findSummonerForName( activeSummonerName.value?: "", reg )
                .doOnError { Log.d( LandingViewModel::class.simpleName, "Error findinmg summoner: ${it.localizedMessage}", it ) }
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
    }
}