package com.tsarsprocket.reportmid

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tsarsprocket.reportmid.model.RegionModel
import com.tsarsprocket.reportmid.model.SummonerModel
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.RuntimeException
import javax.inject.Inject

const val TOP_MASTERIES_NUM = 6

class LandingViewModel @Inject constructor( private val repository: Repository ) : ViewModel() {

    enum class Status { LOADING, UNVERIFIED, VERIFIED }

    val allRegions = repository.allRegions
    val regionTitles
        get() = allRegions.map { it.title }

    val selectedRegion = MutableLiveData<RegionModel>()

    var activeSummonerName = MutableLiveData<String>( "" )

    var activeSummonerModel = MutableLiveData<SummonerModel>()

    val state = MutableLiveData<Status>( Status.LOADING )

    val championImages = Array( TOP_MASTERIES_NUM ) { MutableLiveData<Bitmap>() }

    init {
        viewModelScope.launch {
            val futureSum = async( Dispatchers.IO ) { repository.getActiveSummoner() }
            activeSummonerModel.value = futureSum.await()
            loadMasteries()
            if( activeSummonerModel.value != null ) state.value = Status.VERIFIED
        }
    }

    fun selectRegionByOrderNo( orderNo: Int ) {

        selectedRegion.value = if( orderNo >= 0 && orderNo < allRegions.size ) allRegions[ orderNo ] else null
    }

    fun validateInitial( action: ( fResult: Boolean ) -> Unit ) {

        val reg = enumValues<RegionModel>().find { it == selectedRegion.value } ?: throw RuntimeException( "Incorrect region code \'${selectedRegion}\'" )

        val job = viewModelScope.launch {

            val futureSummoner = async( Dispatchers.IO ) {
                repository.findSummonerForName( activeSummonerName.value?: "", reg ).also { repository.addSummoner( it, true ) }
            }

            activeSummonerModel.value = futureSummoner.await()

            state.value = if( activeSummonerModel.value != null ) Status.VERIFIED else Status.UNVERIFIED

            action( activeSummonerModel.value != null )
        }
    }

    private fun loadMasteries() {
        for( i in 0 until TOP_MASTERIES_NUM ) {
            activeSummonerModel.value!!.masteries[ i ].loadAsync()
                .observeOn( AndroidSchedulers.mainThread() )
                .subscribe() { m ->
                    championImages[ i ].value = m.champion.bitmap
                }
        }
    }
}