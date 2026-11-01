package com.tsarsprocket.reportmid.viewStateApi.viewIntent

import kotlinx.parcelize.Parcelize

@Parcelize
data class LazyViewIntent(
    val intent: ViewIntent,
    val isMaxSized: Boolean,
) : GeneralPurposeViewIntent
