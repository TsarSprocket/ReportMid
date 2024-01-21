package com.tsarsprocket.reportmid.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.rxjava2.cachedIn
import androidx.paging.rxjava2.flowable
import com.tsarsprocket.reportmid.lol.model.PuuidAndRegion
import com.tsarsprocket.reportmid.model.Repository
import com.tsarsprocket.reportmid.summoner_api.model.SummonerModel
import com.tsarsprocket.reportmid.tools.toFlowable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

class MatchHistoryViewModel @Inject constructor(
    private val repository: Repository,
    private val summonerRepository: com.tsarsprocket.reportmid.summoner_api.data.SummonerRepository,
) : ViewModel() {

    val activeSummonerModel = MutableLiveData<SummonerModel>()

    val allDisposables = CompositeDisposable()

    fun initialize( puuidAndRegion: PuuidAndRegion) {
        allDisposables.add( summonerRepository.getByPuuidAndRegion( puuidAndRegion ).toObservable().subscribe { activeSummonerModel.postValue( it ) } )
    }

    @ExperimentalCoroutinesApi
    val flowableMatches = activeSummonerModel.toFlowable()
        .switchMap { (Pager(PagingConfig(pageSize = 8, prefetchDistance = 24)) { repository.getMatchHistoryModel(it.region, it) }).flowable.cachedIn(viewModelScope) }

    override fun onCleared() {
        allDisposables.clear()
        super.onCleared()
    }
}