package com.tsarsprocket.reportmid.matchDetails.impl.viewState

import android.os.Parcelable
import com.tsarsprocket.reportmid.lol.api.presentation_model.ItemInfo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.parcelize.Parcelize


@Parcelize
internal data class PlayerInfo(
    val puuid: String,
    val nameWithTag: String,
    val summonerLevel: Int,
    val championIcon: String,
    val championName: String,
    val championLevel: Int,
    val kills: Int,
    val deaths: Int,
    val assists: Int,
    val runes: RuneSetInfo,
    val summonerSpells: ImmutableList<SummonerSpellInfo>,
    val items: ImmutableList<ItemInfo>,
    val ward: ItemInfo,
) : Parcelable
