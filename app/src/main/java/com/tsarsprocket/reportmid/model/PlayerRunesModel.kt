package com.tsarsprocket.reportmid.model

import com.merakianalytics.orianna.types.core.spectator.Runes

class PlayerRunesModel(val repository: Repository, val shadowRunes: Runes) {
    val primaryPath = repository.dataDragon.tail.getRunePathById(shadowRunes.primaryPath.id)
    val secondaryPath = repository.dataDragon.tail.getRunePathById(shadowRunes.secondaryPath.id)
}