package com.tsarsprocket.reportmid

import androidx.lifecycle.ViewModel
import javax.inject.Inject

class LandingViewModel @Inject constructor() : ViewModel() {

    var summonerName = ""
    private var verifiedName = false

    fun hasVerifiedName() = verifiedName
}