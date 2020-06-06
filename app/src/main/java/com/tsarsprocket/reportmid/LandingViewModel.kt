package com.tsarsprocket.reportmid

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewModelScope
import com.merakianalytics.orianna.types.common.Region
import com.merakianalytics.orianna.types.core.summoner.Summoner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.RuntimeException
import javax.inject.Inject

class LandingViewModel @Inject constructor( private val repository: Repository ) : ViewModel() {

    val regionTags = enumValues<Region>().map { return@map it.tag }
    val selectedRegionTag = MutableLiveData<String>()

    var activeSummonerName = ""

    var activeSummoner: Summoner? = null
        set( value ) {
            field = value
            hasVerifiedNameState.value = value != null
        }

    val hasVerifiedNameState = MutableLiveData<Boolean>( false )

    fun validateInitial( action: ( fResult: Boolean ) -> Unit ) {

        val reg = enumValues<Region>().find { it.tag == selectedRegionTag.value } ?: throw RuntimeException( "Incorrect region code \'${selectedRegionTag}\'" )

        val job = viewModelScope.launch {

            val futureSummoner = async( Dispatchers.IO ) { repository.summonerForName( activeSummonerName, reg ) }

            activeSummoner = futureSummoner.await()

            Log.d( LandingViewModel::class.qualifiedName, "The summoner Id = ${activeSummoner?.id}" )

            action( activeSummoner != null )
        }
    }
}