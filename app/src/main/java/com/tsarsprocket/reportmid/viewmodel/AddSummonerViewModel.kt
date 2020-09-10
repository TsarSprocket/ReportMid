package com.tsarsprocket.reportmid.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tsarsprocket.reportmid.model.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

const val TOP_MASTERIES_NUM = 5

class AddSummonerViewModel @Inject constructor(private val repository: Repository ) : ViewModel() {

    enum class Status { LOADING, UNVERIFIED, VERIFIED }

    val allRegions = Repository.allRegions
    val regionTitles
        get() = allRegions.map { it.title }

    val selectedRegion = MutableLiveData<RegionModel>()

    var activeSummonerName = MutableLiveData( "" )

    var activeSummonerModel = MutableLiveData<SummonerModel>()

    val state = MutableLiveData(Status.LOADING)

    val allDisposables = CompositeDisposable()

    val showSoftInput = MutableLiveData( true )

    val showNotFoundNotifier = MutableLiveData<Int>()

    override fun onCleared() {
        allDisposables.dispose()
        super.onCleared()
    }

    fun selectRegionByOrderNo( orderNo: Int ) {
        selectedRegion.value = if( orderNo >= 0 && orderNo < allRegions.size ) allRegions[ orderNo ] else null
    }

    fun checkSummoner() {
        showSoftInput.value = false
        val reg = enumValues<RegionModel>().find { it == selectedRegion.value } ?: throw RuntimeException( "Incorrect region code \'${selectedRegion}\'" )
        allDisposables.add(
            repository.findSummonerForName( activeSummonerName.value?: "", reg )
                .observeOn( AndroidSchedulers.mainThread() )
                .subscribe( { summonerModel ->
                    activeSummonerModel.value = summonerModel
                    state.value = if( summonerModel != null ) Status.VERIFIED else Status.UNVERIFIED
                } ) {
                    with( showNotFoundNotifier ) {
                        val ct = value
                        value = if( ct == null ) 0 else ct + 1
                    }
                }
        )
    }

}