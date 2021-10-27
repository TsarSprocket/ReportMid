package com.tsarsprocket.reportmid.viewmodel

import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tsarsprocket.reportmid.model.PuuidAndRegion
import com.tsarsprocket.reportmid.model.Repository
import com.tsarsprocket.reportmid.summoner.model.SummonerRepository
import io.reactivex.BackpressureStrategy
import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class LandingViewModel @Inject constructor(
    val repository: Repository,
    private val summonerRepository: SummonerRepository,
) : ViewModel() {

    enum class STATE { LOADING, FOUND, NOT_FOUND }

    var puuid = PuuidAndRegion.NONE
    val stateLive = MutableLiveData(STATE.LOADING)

    val disposer = CompositeDisposable()

    init {
        disposer.add(repository.getActiveSummonerPuuidAndRegion()
            .observeOn(AndroidSchedulers.mainThread())
            .map { puuidAndRegion ->
                Pair(STATE.FOUND, puuidAndRegion)
            }
            .concatWith(Single.just(Pair(STATE.NOT_FOUND, PuuidAndRegion.NONE)))
            .take(1)
            .subscribe {
                puuid = it.second
                stateLive.value = it.first
            })
    }

    fun defineMainAccount(puuidAndRegion: PuuidAndRegion) =
        LiveDataReactiveStreams.fromPublisher(
            summonerRepository.getByPuuidAndRegion(puuidAndRegion).flatMapObservable { summonerModel ->
                repository.addMyAccountNotify(summonerModel, true)
            }.toFlowable(BackpressureStrategy.LATEST)
        )

    override fun onCleared() {
        disposer.clear()
        super.onCleared()
    }
}