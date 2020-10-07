package com.tsarsprocket.reportmid.viewmodel

import androidx.annotation.MainThread
import androidx.lifecycle.*
import com.tsarsprocket.reportmid.model.PuuidAndRegion
import com.tsarsprocket.reportmid.model.Repository
import com.tsarsprocket.reportmid.model.SummonerModel
import com.tsarsprocket.reportmid.tools.toLiveData
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ManageSummonersViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    val mySummonersLive: LiveData<List<SummonerModel>> = repository.getMySummonersObservable().toLiveData()

    val checkedSummoners = HashSet<SummonerModel>()

    private val disposer = CompositeDisposable()

    //  Methods  ///////////////////////////////////////////////////////////////

    fun addMySummoner(puuidAndRegion: PuuidAndRegion) {
        disposer.add(repository.findSummonerByPuuidAndRegion( puuidAndRegion ).switchMap { summonerModel ->
            repository.addMyAccountNotify( summonerModel ) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe())
    }

    fun deleteSelected() {
        val selected = checkedSummoners.toList()
        if (selected.size == (mySummonersLive.value?.size ?: 0)) throw CannotDeleteAllSummoners()
        if(selected.isEmpty()) throw CannotDeleteNothing()
        repository.deleteMySummonersSwitchActive(checkedSummoners.toList())
    }

    override fun onCleared() {
        disposer.clear()
    }

    //  Local Exceptions  /////////////////////////////////////////////////////

    class CannotDeleteAllSummoners: RuntimeException()
    class CannotDeleteNothing: RuntimeException()
}