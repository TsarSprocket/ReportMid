package com.tsarsprocket.reportmid.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tsarsprocket.reportmid.lol.model.PuuidAndRegion
import com.tsarsprocket.reportmid.model.Repository
import com.tsarsprocket.reportmid.summonerApi.data.SummonerRepository
import com.tsarsprocket.reportmid.tools.toLiveData
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.rx2.rxSingle
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

    fun defineMainAccount(puuidAndRegion: PuuidAndRegion) = rxSingle { summonerRepository.requestRemoteSummonerByPuuidAndRegion(puuidAndRegion) }.flatMapObservable { summonerModel ->
        repository.addMyAccountNotify(summonerModel, true)
    }.toLiveData()

    override fun onCleared() {
        disposer.clear()
        super.onCleared()
    }
}