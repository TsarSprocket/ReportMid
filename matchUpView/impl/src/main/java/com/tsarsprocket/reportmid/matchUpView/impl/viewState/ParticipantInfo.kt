package com.tsarsprocket.reportmid.matchUpView.impl.viewState

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class ParticipantInfo(
    val championImageUrl: String,
    val summonerDisplayName: String,
    val primaryRuneImageUrls: List<String>,
    val secondaryRuneImageUrls: List<String>,
    val summonerSpell1ImageUrl: String,
    val summonerSpell2ImageUrl: String,
) : Parcelable
