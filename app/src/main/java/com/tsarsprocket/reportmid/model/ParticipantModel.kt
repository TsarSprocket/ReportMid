package com.tsarsprocket.reportmid.model

import com.merakianalytics.orianna.types.core.match.Participant

class ParticipantModel( private val repository: Repository, val team: TeamModel, private val shadowParticipant: Participant ) {
    val summoner = repository.getSummonerModel( shadowParticipant.summoner )
    val champion = repository.getChampionModel( shadowParticipant.champion )
    val kills = shadowParticipant.stats.kills
    val deaths = shadowParticipant.stats.deaths
    val assists = shadowParticipant.stats.assists
    val isWinner = shadowParticipant.stats.isWinner
}