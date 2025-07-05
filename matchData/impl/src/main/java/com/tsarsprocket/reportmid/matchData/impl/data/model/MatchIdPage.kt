package com.tsarsprocket.reportmid.matchData.impl.data.model

import com.tsarsprocket.reportmid.requestManagerApi.data.RequestResult

internal data class MatchIdPage(
    val matchIds: List<String>,
) : RequestResult