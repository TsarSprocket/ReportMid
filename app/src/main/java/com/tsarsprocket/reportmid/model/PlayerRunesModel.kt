package com.tsarsprocket.reportmid.model

import com.merakianalytics.orianna.types.core.spectator.Runes

class PlayerRunesModel(val repository: Repository, val shadowRunes: Runes) {
    val primaryPath = repository.dataDragon.tailSubject.value?.getRunePathById(shadowRunes.primaryPath.id)
        ?: throw IncorrectRunePathIdException(shadowRunes.primaryPath.id)
    val secondaryPath = repository.dataDragon.tailSubject.value?.getRunePathById(shadowRunes.secondaryPath.id)
        ?: throw IncorrectRunePathIdException(shadowRunes.secondaryPath.id)
}