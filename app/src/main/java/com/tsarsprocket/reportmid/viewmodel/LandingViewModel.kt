package com.tsarsprocket.reportmid.viewmodel

import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tsarsprocket.reportmid.model.Repository
import com.tsarsprocket.reportmid.model.SummonerModel
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class LandingViewModel @Inject constructor( val repository: Repository ) : ViewModel() {

    enum class STATE { LOADING, FOUND, NOT_FOUND }

    var puuid = ""
    val stateLive = MutableLiveData(STATE.LOADING)

    val disposer = CompositeDisposable()

    var beenThereDoneThat = false

    init {
        disposer.add( repository.getActiveSummonerPUUID()
            .observeOn( AndroidSchedulers.mainThread() )
            .doOnNext{ puuid ->
                this.puuid = puuid
                stateLive.value = STATE.FOUND
            }
            .doOnComplete { if( stateLive.value != STATE.FOUND) stateLive.value = STATE.NOT_FOUND }
            .subscribe() )
    }

    fun defineMainAccount( puuid: String ) =
        LiveDataReactiveStreams.fromPublisher(
            repository.findSummonerByPuuid( puuid ).flatMap { summonerModel ->
                repository.getMyAccountAdder( summonerModel, true )
            }.toFlowable( BackpressureStrategy.LATEST )
        )

    override fun onCleared() {
        disposer.clear()
        super.onCleared()
    }
}