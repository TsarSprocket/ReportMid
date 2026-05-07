package com.tsarsprocket.reportmid.currentGameData.api.data.model

import com.tsarsprocket.reportmid.lol.api.domain.model.Perk
import com.tsarsprocket.reportmid.lol.api.domain.model.Rune

data class Runes(
    val primaryRunes: List<Rune>,
    val secondaryRunes: List<Rune>,
    val statPerks: List<Perk>,
)