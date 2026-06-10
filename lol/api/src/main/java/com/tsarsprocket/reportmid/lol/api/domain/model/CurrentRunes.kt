package com.tsarsprocket.reportmid.lol.api.domain.model

data class CurrentRunes(
    val rune: Rune,
    val primaryStyle: RunePath,
    val secondaryStyle: RunePath,
)
