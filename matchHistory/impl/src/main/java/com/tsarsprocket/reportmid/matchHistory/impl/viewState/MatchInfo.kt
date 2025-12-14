package com.tsarsprocket.reportmid.matchHistory.impl.viewState

import android.os.Parcelable
import com.tsarsprocket.reportmid.lol.api.presentation.model.ChampionInfo
import com.tsarsprocket.reportmid.lol.api.presentation.model.ItemInfo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.parcelize.Parcelize

@Parcelize
internal class MatchInfo(
    val matchId: String,
    val gameOutcome: GameOutcome,
    val self: ChampionInfo,
    val gameType: String,
    val isSummonerRift: Boolean,
    val kills: String,
    val deaths: String,
    val assists: String,
    val items: ImmutableList<ImmutableList<ItemInfo>>, // rows, then columns
    val ward: ItemInfo,
    val teams: ImmutableList<ImmutableList<ImmutableList<ChampionInfo?>>>,
) : ItemToShow, Parcelable
