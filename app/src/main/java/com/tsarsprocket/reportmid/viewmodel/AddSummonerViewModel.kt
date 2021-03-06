package com.tsarsprocket.reportmid.viewmodel

import androidx.lifecycle.*
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

    val activeSummonerName = MutableLiveData( "" )

    val activeSummonerModel = MutableLiveData<SummonerModel>()

    val selectedRegionName = selectedRegionPosition.map { pos -> if (pos>=0) allRegions[pos].title else "<not selected>" }

    //  Methods  //////////////////////////////////////////////////////////////

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

    fun isSummonerInUseLive(sum: SummonerModel): LiveData<Boolean> =
        LiveDataReactiveStreams.fromPublisher(repository.checkSummonerExistInDB(sum).toFlowable(BackpressureStrategy.LATEST))
}
