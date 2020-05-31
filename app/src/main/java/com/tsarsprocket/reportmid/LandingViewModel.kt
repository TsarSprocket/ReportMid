package com.tsarsprocket.reportmid

import android.widget.Spinner
import androidx.databinding.InverseBindingMethod
import androidx.databinding.InverseBindingMethods
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

    fun onValidateInitial() {

        activeSummoner = repository.summonerForName( activeSummonerName )
    }
}