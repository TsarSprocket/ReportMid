package com.tsarsprocket.reportmid.model

import com.merakianalytics.orianna.types.core.spectator.CurrentMatch
import java.util.*

class CurrentMatchModel( val repository: Repository, private val shadowCurrentMatch: CurrentMatch ) {
    val blueTeam by lazy { repository.getCurrentMatchTeam{ shadowCurrentMatch.blueTeam } }
    val redTeam by lazy { repository.getCurrentMatchTeam{ shadowCurrentMatch.redTeam } }
    val creationMoment = Calendar.getInstance().apply{ add( Calendar.MILLISECOND, -shadowCurrentMatch.duration.millis.toInt() ) }
    val gameType = GameTypeModel.by( shadowCurrentMatch.type, shadowCurrentMatch.queue, shadowCurrentMatch.mode, shadowCurrentMatch.map )
}