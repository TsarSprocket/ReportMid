package com.tsarsprocket.reportmid

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.merakianalytics.orianna.types.common.Region
import com.merakianalytics.orianna.types.core.summoner.Summoner
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

    fun validateInitial(): Boolean {

        val reg = enumValues<Region>().find { it.tag == selectedRegionTag.value } ?: return false

        activeSummoner = repository.summonerForName( activeSummonerName, reg )

        TODO( "Fetch summoner parameters" )

        Log.d( LandingViewModel::class.qualifiedName, "The summoner Id = ${activeSummoner?.id}" )

        return activeSummoner != null
    }
}