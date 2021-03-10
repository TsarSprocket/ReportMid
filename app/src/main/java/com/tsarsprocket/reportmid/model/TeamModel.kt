package com.tsarsprocket.reportmid.model

import com.merakianalytics.orianna.types.core.match.Team
import com.tsarsprocket.reportmid.riotapi.matchV4.ParticipantDto
import com.tsarsprocket.reportmid.riotapi.matchV4.ParticipantIdentityDto

class TeamModel(
    val repository: Repository,
    val match: MatchModel,
    lstMemberDtos: List<Pair<ParticipantDto, ParticipantIdentityDto>>,
    region: RegionModel,
//    private val shadowTeam: Team
) {
    enum class TeamColor(val colorCode: Int) { BLUE(100), RED(200) }

    val participants = lstMemberDtos.map { (participant, identity) ->  ParticipantModel(repository, this, participant, identity, region) }
}