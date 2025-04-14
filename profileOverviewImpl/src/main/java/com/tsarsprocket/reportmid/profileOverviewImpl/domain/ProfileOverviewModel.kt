package com.tsarsprocket.reportmid.profileOverviewImpl.domain

internal data class ProfileOverviewModel(
    val gameName: String,
    val tagLine: String,
    val imageUrl: String,
    val level: Int,
    val masteries: List<MasteryOverview>,
)
