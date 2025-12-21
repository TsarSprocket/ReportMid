package com.tsarsprocket.reportmid.viewStateApi.viewIntent

import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import kotlinx.parcelize.Parcelize

@Parcelize
class ShowStateViewIntent(
    val state: ViewState,
) : GeneralPurposeViewIntent
