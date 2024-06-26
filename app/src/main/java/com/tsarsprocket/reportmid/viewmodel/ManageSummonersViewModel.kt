package com.tsarsprocket.reportmid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.tsarsprocket.reportmid.lol.model.PuuidAndRegion
import com.tsarsprocket.reportmid.model.Repository
import com.tsarsprocket.reportmid.summonerApi.model.Summoner
import com.tsarsprocket.reportmid.tools.toLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.rx2.rxObservable
import kotlinx.coroutines.rx2.rxSingle
import javax.inject.Inject

class ManageSummonersViewModel @Inject constructor(
    private val repository: Repository,
    private val summonerRepository: com.tsarsprocket.reportmid.summonerApi.data.SummonerRepository,
) : ViewModel() {

    val mySummonersLive: LiveData<List<Summoner>> = rxSingle { summonerRepository.getMySummoners() }.toObservable().toLiveData()

    val checkedSummoners = HashSet<Summoner>()

    private val disposer = CompositeDisposable()

    //  Methods  ///////////////////////////////////////////////////////////////

    fun addMySummoner(puuidAndRegion: PuuidAndRegion) {
        disposer.add(rxObservable<Summoner> { summonerRepository.requestRemoteSummonerByPuuidAndRegion(puuidAndRegion) }
            .switchMap { summonerModel -> repository.addMyAccountNotify(summonerModel) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe())
    }

    fun deleteSelected() {
        val selected = checkedSummoners.toList()
        if(selected.size == (mySummonersLive.value?.size ?: 0)) throw CannotDeleteAllSummoners()
        if(selected.isEmpty()) throw CannotDeleteNothing()
        repository.deleteMySummonersSwitchActive(checkedSummoners.toList())
    }

    override fun onCleared() {
        disposer.clear()
    }

    //  Local Exceptions  /////////////////////////////////////////////////////

    class CannotDeleteAllSummoners : RuntimeException()
    class CannotDeleteNothing : RuntimeException()
}