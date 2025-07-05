package com.tsarsprocket.reportmid.matchData.api.data.model

import com.tsarsprocket.reportmid.lol.api.model.Rune
import com.tsarsprocket.reportmid.lol.api.model.RunePath

data class RuneStyle(
    val path: RunePath,
    val runes: List<Rune>,
)