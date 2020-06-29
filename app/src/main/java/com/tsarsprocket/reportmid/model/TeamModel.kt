package com.tsarsprocket.reportmid.model

import com.merakianalytics.orianna.types.core.match.Team

class TeamModel(
    val repository: Repository,
    private val shadowTeam: Team
) {
    val participants = List<ParticipantModel>( shadowTeam.participants.size ) { i ->
        repository.getParticipantModel( this, shadowTeam.participants[ i ] )
    }
}