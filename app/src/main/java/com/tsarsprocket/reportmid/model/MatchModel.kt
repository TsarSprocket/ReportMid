package com.tsarsprocket.reportmid.model

import com.merakianalytics.orianna.types.core.match.Match

class MatchModel( private val repository: Repository, private val shadowMatch: Match ) {
    val id = shadowMatch.id
    val blueTeam = repository.getTeamModel( shadowMatch.blueTeam )
    val redTeam = repository.getTeamModel( shadowMatch.redTeam )
    val teams = arrayOf( blueTeam, redTeam )
}