package com.tsarsprocket.reportmid.model

import com.merakianalytics.orianna.types.core.spectator.Runes

class PlayerRunesModel( val repository: Repository, val shadowRunes: Runes ) {
    val primaryPath = RunePathModel.byId[ shadowRunes.primaryPath.id ]?: throw IncorrectRunePathIdException( shadowRunes.primaryPath.id )
    val secondaryPath = RunePathModel.byId[ shadowRunes.secondaryPath.id ]?: throw IncorrectRunePathIdException( shadowRunes.secondaryPath.id )
}