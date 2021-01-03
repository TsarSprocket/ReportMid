package com.tsarsprocket.reportmid.riotapi.spectatorV4

import com.google.gson.annotations.SerializedName

data class CurrentGameParticipant(
    @SerializedName("championId")               val championId:                 Long,
    @SerializedName("perks")                    val perks:                      Perks,
    @SerializedName("profileIconId")            val profileIconId:              Long,
    @SerializedName("bot")                      val bot:                        Boolean,
    @SerializedName("teamId")                   val teamId:                     Long,
    @SerializedName("summonerName")             val summonerName:               String,
    @SerializedName("summonerId")               val summonerId:                 String,
    @SerializedName("spell1Id")                 val spell1Id:                   Long,
    @SerializedName("spell2Id")                 val spell2Id:                   Long,
    @SerializedName("gameCustomizationObjects") val gameCustomizationObjects:   List<GameCustomizationObject>,
)