package com.tsarsprocket.reportmid.landing.viewmodel

import androidx.lifecycle.ViewModel
import com.tsarsprocket.reportmid.landing.usecase.LandingUseCase
import javax.inject.Inject

class LandingViewModel @Inject constructor(
    private val landingUseCase: LandingUseCase
) : ViewModel() {

}