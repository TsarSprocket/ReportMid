package com.tsarsprocket.reportmid.findSummonerImpl.viewEffect

import androidx.annotation.StringRes
import com.tsarsprocket.reportmid.kspApi.annotation.Effect
import com.tsarsprocket.reportmid.viewStateApi.viewEffect.ViewEffect

@Effect
internal class ShowSnackViewEffect(@StringRes val text: Int) : ViewEffect
