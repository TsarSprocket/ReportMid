package com.tsarsprocket.reportmid.model

import com.merakianalytics.orianna.types.core.match.Team
import com.tsarsprocket.reportmid.di.assisted.ParticipantModelFactory
import com.tsarsprocket.reportmid.riotapi.matchV4.ParticipantDto
import com.tsarsprocket.reportmid.riotapi.matchV4.ParticipantIdentityDto
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class TeamModel @AssistedInject constructor(
    @Assisted val match: MatchModel,
    @Assisted lstMemberDtos: List<Pair<ParticipantDto, ParticipantIdentityDto>>,
    @Assisted region: RegionModel,
    private val repository: Repository,
    private val participantModelFactory: ParticipantModelFactory,
    ) {
    enum class TeamColor(val colorCode: Int) { BLUE(100), RED(200) }

    val participants = lstMemberDtos.map { (participant, identity) -> participantModelFactory.create( this, participant, identity, region ) }
}