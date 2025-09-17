package com.tsarsprocket.reportmid.matchHistory.impl.viewIntent

import com.tsarsprocket.reportmid.kspApi.annotation.Intent
import kotlinx.parcelize.Parcelize

@Parcelize
@Intent
internal data object LoadMoreIntent : InternalMatchHistoryIntent
