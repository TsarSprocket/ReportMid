package com.tsarsprocket.reportmid.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tsarsprocket.reportmid.model.ChampionMasteryModel
import com.tsarsprocket.reportmid.model.Repository
import com.tsarsprocket.reportmid.model.SummonerModel
import com.tsarsprocket.reportmid.presentation.MasteryLive
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ProfileOverviewViewModel @Inject constructor( private val repository: Repository ): ViewModel() {

    var activeSummonerModel = MutableLiveData<SummonerModel>()
    val masteries = Array( TOP_MASTERIES_NUM ) { MasteryLive() }
    val allDisposables = CompositeDisposable()
    val summonerDisposables = CompositeDisposable()

    fun initialize( puuid: String ) {
        allDisposables.add( repository.findSummonerByPuuid( puuid ).subscribe { activeSummonerModel.postValue( it ) } )
        observeMasteries()
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

    override fun onCleared() {
        summonerDisposables.clear()
        allDisposables.clear()
        super.onCleared()
    }
}