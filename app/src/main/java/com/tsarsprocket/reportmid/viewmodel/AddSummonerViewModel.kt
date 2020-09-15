package com.tsarsprocket.reportmid.viewmodel

import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.tsarsprocket.reportmid.model.*
import io.reactivex.BackpressureStrategy
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

const val TOP_MASTERIES_NUM = 5

class AddSummonerViewModel @Inject constructor(private val repository: Repository ) : ViewModel() {

    val allRegions = Repository.allRegions
    val regionTitles
        get() = allRegions.map { it.title }

    val selectedRegionPosition = MutableLiveData<Int>()

    var activeSummonerName = MutableLiveData( "" )

    var activeSummonerModel = MutableLiveData<SummonerModel>()


    fun checkSummoner() =
        LiveDataReactiveStreams.fromPublisher(
            repository.findSummonerForName( activeSummonerName.value?: "",
                allRegions[ selectedRegionPosition.value!! ], failOnNotFound = true )
                .observeOn( AndroidSchedulers.mainThread() )
                .map { summonerModel ->
                    Maybe.just( summonerModel )
                }.onErrorReturn {
                    Maybe.empty()
                }
                .toFlowable( BackpressureStrategy.LATEST )
        ).map {
            if( !it.isEmpty.blockingGet() ) activeSummonerModel.value = it.blockingGet()
            it
        }
}
