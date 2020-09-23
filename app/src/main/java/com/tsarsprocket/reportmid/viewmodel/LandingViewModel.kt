package com.tsarsprocket.reportmid.viewmodel

import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tsarsprocket.reportmid.model.Repository
import io.reactivex.BackpressureStrategy
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class LandingViewModel @Inject constructor(val repository: Repository) : ViewModel() {

    enum class STATE { LOADING, FOUND, NOT_FOUND }

    var puuid = ""
    val stateLive = MutableLiveData(STATE.LOADING)

    val disposer = CompositeDisposable()

    init {
        disposer.add(repository.getActiveSummonerPUUID()
            .observeOn(AndroidSchedulers.mainThread())
            .map { puuid ->
                Pair(STATE.FOUND, puuid)
            }
            .concatWith(Single.just(Pair(STATE.NOT_FOUND, "")))
            .take(1)
            .subscribe {
                puuid = it.second
                stateLive.value = it.first
            })
    }

    fun defineMainAccount(puuid: String) =
        LiveDataReactiveStreams.fromPublisher(
            repository.findSummonerByPuuid(puuid).flatMap { summonerModel ->
                repository.addMyAccountNotify(summonerModel, true)
            }.toFlowable(BackpressureStrategy.LATEST)
        )

    override fun onCleared() {
        disposer.clear()
        super.onCleared()
    }
}