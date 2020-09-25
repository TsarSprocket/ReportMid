package com.tsarsprocket.reportmid.viewmodel

import androidx.annotation.MainThread
import androidx.lifecycle.*
import com.tsarsprocket.reportmid.model.Repository
import com.tsarsprocket.reportmid.model.SummonerModel
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ManageMySummonersViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val forceUpdateCounter = MutableLiveData(0)

    val mySummonersLive = forceUpdateCounter.switchMap {
        LiveDataReactiveStreams.fromPublisher(repository.getMySummonersObservable().toFlowable(BackpressureStrategy.BUFFER)) }

    val checkedSummoners = HashSet<SummonerModel>()

    private val disposer = CompositeDisposable()

    //  Methods  ///////////////////////////////////////////////////////////////

    fun addMySummoner(puuid: String) {
        disposer.add(repository.findSummonerByPuuid( puuid ).flatMap { summonerModel ->
            repository.addMyAccountNotify( summonerModel ) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{ forceUpdate() })
    }

    @MainThread
    fun forceUpdate() {
        forceUpdateCounter.value = forceUpdateCounter.value?.plus(1)
    }

    override fun onCleared() {
        disposer.clear()
    }
}