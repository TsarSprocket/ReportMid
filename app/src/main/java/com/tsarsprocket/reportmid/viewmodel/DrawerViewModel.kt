package com.tsarsprocket.reportmid.viewmodel

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.*
import com.tsarsprocket.reportmid.model.RegionModel
import com.tsarsprocket.reportmid.model.Repository
import com.tsarsprocket.reportmid.model.SummonerModel
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DrawerViewModel @Inject constructor(val repository: Repository) : ViewModel() {

    val currentRegions = repository.getCurrentRegions()
    val regionTitles =
        LiveDataReactiveStreams.fromPublisher(currentRegions.map { list -> list.map { reg -> reg.title } }.toFlowable(BackpressureStrategy.LATEST))
    val selectedRegionPosition = MutableLiveData<Int>()
    val mySummonersInSelectedRegion = selectedRegionPosition.map { currentRegions.value!![it] }.switchMap { reg ->
        LiveDataReactiveStreams.fromPublisher<List<Triple<Bitmap,String,Boolean>>> (
            repository.getMySummonersObservableForRegion( reg )
                .map{ lst -> lst.map{ (sum,isSelected) -> Triple( sum.icon.blockingFirst(), sum.name, isSelected ) } }
                .toFlowable(BackpressureStrategy.BUFFER)
        )
    }

    val disposer = CompositeDisposable()

    init {
        disposer.add(
            repository.getSelectedAccountRegion()
                .zipWith(currentRegions) { a, b -> Pair(a, b) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { (curReg, lstReds) ->
                    selectedRegionPosition.value = lstReds.indexOf(curReg)
                }
        )
    }
}