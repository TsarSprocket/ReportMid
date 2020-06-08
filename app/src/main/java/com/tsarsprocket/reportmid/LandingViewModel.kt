package com.tsarsprocket.reportmid

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tsarsprocket.reportmid.model.Region
import com.tsarsprocket.reportmid.model.Summoner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.RuntimeException
import javax.inject.Inject

class LandingViewModel @Inject constructor( private val repository: Repository ) : ViewModel() {

    val allRegions = repository.allRegions
    val regionTitles
        get() = allRegions.map { it.title }

    val selectedRegion = MutableLiveData<Region>()

    var activeSummonerName = ""

    var activeSummoner: Summoner? = null
        set( value ) {
            field = value
            hasVerifiedNameState.value = value != null
        }

    val hasVerifiedNameState = MutableLiveData<Boolean>( false )

    fun selectRegionByOrderNo( orderNo: Int ) {

        selectedRegion.value = if( orderNo >= 0 && orderNo < allRegions.size ) allRegions[ orderNo ] else null
    }


    fun validateInitial( action: ( fResult: Boolean ) -> Unit ) {

        val reg = enumValues<Region>().find { it == selectedRegion.value } ?: throw RuntimeException( "Incorrect region code \'${selectedRegion}\'" )

        val job = viewModelScope.launch {

            val futureSummoner = async( Dispatchers.IO ) { repository.summonerForName( activeSummonerName, reg ) }

            activeSummoner = futureSummoner.await()

            action( activeSummoner != null )
        }
    }
}