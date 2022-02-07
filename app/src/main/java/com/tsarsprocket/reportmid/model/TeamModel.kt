package com.tsarsprocket.reportmid.model

import com.tsarsprocket.reportmid.di.assisted.ParticipantModelFactory
import com.tsarsprocket.reportmid.riotapi.matchV5.ParticipantDto
import com.tsarsprocket.reportmid.riotapi.matchV5.ParticipantIdentityDto
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class TeamModel @AssistedInject constructor(
    @Assisted val match: MatchModel,
    @Assisted participantDtos: List<ParticipantDto>,
    @Assisted region: RegionModel,
    private val participantModelFactory: ParticipantModelFactory,
    ) {
    enum class TeamColor(val colorCode: Int) { BLUE(100), RED(200) }

    val participants = participantDtos.map { participant -> participantModelFactory.create( this, participant, region ) }
}