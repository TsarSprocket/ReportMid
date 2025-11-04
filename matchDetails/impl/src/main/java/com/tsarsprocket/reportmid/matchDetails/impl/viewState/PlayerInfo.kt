package com.tsarsprocket.reportmid.matchDetails.impl.viewState

import android.os.Parcelable
import com.tsarsprocket.reportmid.lol.api.presentation_model.ItemInfo
import kotlinx.parcelize.Parcelize


@Parcelize
internal data class PlayerInfo(
    val puuid: String,
    val nameWithTag: String,
    val summonerLevel: Int,
    val championIcon: String,
    val championLevel: Int,
    val kills: Int,
    val deaths: Int,
    val assists: Int,
    val runes: RuneSetInfo,
    val summonerSpells: List<String>,
    val items: List<ItemInfo>,
    val ward: ItemInfo,
) : Parcelable
