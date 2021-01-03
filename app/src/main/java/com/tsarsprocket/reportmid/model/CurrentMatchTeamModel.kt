package com.tsarsprocket.reportmid.model

import com.tsarsprocket.reportmid.riotapi.spectatorV4.CurrentGameParticipant
import com.tsarsprocket.reportmid.riotapi.spectatorV4.Team

class CurrentMatchTeamModel(repo: Repository, col: Team, participants: List<CurrentGameParticipant>, region: RegionModel) {
    private val repository = repo
    val color = col
    val participants: List<PlayerModel> = participants.map{ PlayerModel(repo, it, region) }
}