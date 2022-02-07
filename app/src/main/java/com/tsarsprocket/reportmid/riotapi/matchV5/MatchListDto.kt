package com.tsarsprocket.reportmid.riotapi.matchV5
import com.google.gson.annotations.SerializedName


data class MatchListDto(
    @SerializedName("startIndex") val startIndex: Int,
    @SerializedName("totalGames") val totalGames: Int,
    @SerializedName("endIndex") val endIndex: Int,
    @SerializedName("matches") val matches: List<MatchReferenceDto>,
)
