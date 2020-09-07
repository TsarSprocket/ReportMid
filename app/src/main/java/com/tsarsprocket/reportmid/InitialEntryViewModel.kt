package com.tsarsprocket.reportmid

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tsarsprocket.reportmid.model.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

const val TOP_MASTERIES_NUM = 5

class InitialEntryViewModel @Inject constructor(private val repository: Repository ) : ViewModel() {

    enum class Status { LOADING, UNVERIFIED, VERIFIED }

    val allRegions = Repository.allRegions
    val regionTitles
        get() = allRegions.map { it.title }

    val selectedRegion = MutableLiveData<RegionModel>()

    var activeSummonerName = MutableLiveData( "" )

    var activeSummonerModel = MutableLiveData<SummonerModel>()

    val state = MutableLiveData( Status.LOADING )

    val allDisposables = CompositeDisposable()

/*
    init {
        allDisposables.add( repository.getActiveSummoner()
            .observeOn( AndroidSchedulers.mainThread() )
            .doOnNext { summonerModel ->
                activeSummonerModel.value = summonerModel
                state.value = Status.VERIFIED
            }
            .doOnComplete {
                if( activeSummonerModel.value == null ) state.value = Status.UNVERIFIED
            }
            .doOnError { e -> Log.d( InitialEntryViewModel::class.simpleName, "Cannot initialize LandingViewModel", e ) }
            .subscribe() )
    }
*/

    override fun onCleared() {
        allDisposables.dispose()
        super.onCleared()
    }

    fun selectRegionByOrderNo( orderNo: Int ) {
        selectedRegion.value = if( orderNo >= 0 && orderNo < allRegions.size ) allRegions[ orderNo ] else null
    }

    fun validateInitial() {
        val reg = enumValues<RegionModel>().find { it == selectedRegion.value } ?: throw RuntimeException( "Incorrect region code \'${selectedRegion}\'" )
        allDisposables.add(
            repository.findSummonerForName( activeSummonerName.value?: "", reg )
                .doOnError { Log.d( InitialEntryViewModel::class.simpleName, "Error findinmg summoner: ${it.localizedMessage}", it ) }
                .observeOn( Schedulers.io() )
                .doOnNext { summonerModel ->
                    repository.addSummoner( summonerModel, true )
                }
                .observeOn( AndroidSchedulers.mainThread() )
                .subscribe { summonerModel ->
                    activeSummonerModel.value = summonerModel
                    state.value = if( summonerModel != null ) Status.VERIFIED else Status.UNVERIFIED
                }
        )
    }

}