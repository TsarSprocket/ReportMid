package com.tsarsprocket.reportmid.viewmodel

import android.view.View
import androidx.lifecycle.*
import com.tsarsprocket.reportmid.model.Repository
import io.reactivex.BackpressureStrategy
import io.reactivex.subjects.ReplaySubject
import javax.inject.Inject

class ConfirmSummonerViewModel @Inject constructor( val repository: Repository ): ViewModel() {

    val puuidSubj = ReplaySubject.create<String>( 1 )
    val summoner = LiveDataReactiveStreams.fromPublisher( puuidSubj.flatMap { puuid -> repository.findSummonerByPuuid( puuid ) }.toFlowable( BackpressureStrategy.LATEST ) )
    val bitmap = summoner.switchMap { sum -> LiveDataReactiveStreams.fromPublisher( sum.icon.toFlowable( BackpressureStrategy.LATEST ) ) }
    val name = summoner.map { sum -> sum.name }
    val level = summoner.map { sum -> sum.level.toString() }
    val confirm = MutableLiveData<Boolean>()

    fun init( puuid: String ) {
        if( puuidSubj.value != puuid ) puuidSubj.onNext( puuid )
    }

    fun confirm( view: View ) {
        confirm.value = true
    }

    fun decline( view: View ) {
        confirm.value = false
    }
}