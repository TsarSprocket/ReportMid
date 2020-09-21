package com.tsarsprocket.reportmid.viewmodel

import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.tsarsprocket.reportmid.model.Repository
import com.tsarsprocket.reportmid.model.SummonerModel
import io.reactivex.BackpressureStrategy
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ManageMySummonersViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    val mySummonersLive = LiveDataReactiveStreams.fromPublisher(repository.getMySummonersObservable().toFlowable(BackpressureStrategy.BUFFER))
}