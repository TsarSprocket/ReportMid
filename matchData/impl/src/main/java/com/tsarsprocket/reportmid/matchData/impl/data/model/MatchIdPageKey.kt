package com.tsarsprocket.reportmid.matchData.impl.data.model

import com.tsarsprocket.reportmid.lol.api.model.Region
import com.tsarsprocket.reportmid.requestManagerApi.data.RequestKey

internal data class MatchIdPageKey(
    val puuid: String,
    val region: Region,
    val pageNo: Int,
) : RequestKey
