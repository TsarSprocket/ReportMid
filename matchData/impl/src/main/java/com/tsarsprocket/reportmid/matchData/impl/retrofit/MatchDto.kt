package com.tsarsprocket.reportmid.matchData.impl.retrofit

import com.google.gson.annotations.SerializedName
import com.tsarsprocket.reportmid.requestManagerApi.data.RequestResult

internal data class MatchDto(
    @SerializedName("metadata") val metadata: MetadataDto,
    @SerializedName("info") val info: InfoDto,
) : RequestResult
