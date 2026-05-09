package com.tsarsprocket.reportmid.lol.api.domain.model

data class Runes(
    val primaryRunes: List<Rune>,
    val secondaryRunes: List<Rune>,
    val statPerks: List<Perk>,
)