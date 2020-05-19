package com.tsarsprocket.reportmid

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class LandingViewModel @Inject constructor() : ViewModel() {

    var initialSummonerName = ""

    val hasVerifiedNameState = MutableLiveData<Boolean>( false )

    fun onValidateInitial() {

        // TODO
    }
}