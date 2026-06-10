package com.tsarsprocket.reportmid.matchUpView.impl.viewState

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class ParticipantInfo(
    val player: PlayerInfo,
    val championImageUrl: String,
    val primaryRune: RuneIconInfo,
    val secondaryRuneStyle: RuneIconInfo,
    val summonerSpell1ImageUrl: String,
    val summonerSpell2ImageUrl: String,
) : Parcelable
