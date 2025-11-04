package com.tsarsprocket.reportmid.matchDetails.impl.viewState

import android.os.Parcelable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class TeamInfo(
    val gameOutcome: String,
    val teamSide: TeamSide,
    val players: ImmutableList<PlayerInfo>,
) : Parcelable
