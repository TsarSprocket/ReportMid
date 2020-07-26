package com.tsarsprocket.reportmid.model

import com.merakianalytics.orianna.types.core.match.Match
import java.lang.IllegalArgumentException

class MatchModel( private val repository: Repository, private val shadowMatch: Match ) {
    val id = shadowMatch.id
    val blueTeam by lazy { repository.getTeamModel( shadowMatch.blueTeam ).replay( 1 ).autoConnect() }
    val redTeam by lazy { repository.getTeamModel( shadowMatch.redTeam ).replay( 1 ).autoConnect() }
    val teams by lazy { arrayOf( blueTeam, redTeam ) }
    val gameType = try{ Repository.getGameType( shadowMatch.type, shadowMatch.queue, shadowMatch.mode, shadowMatch.map ) } catch ( ex: IllegalArgumentException ) { GameTypeModel.UNKNOWN }
    val remake = shadowMatch.isRemake
}