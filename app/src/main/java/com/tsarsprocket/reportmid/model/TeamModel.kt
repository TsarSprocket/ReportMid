package com.tsarsprocket.reportmid.model

import com.tsarsprocket.reportmid.di.assisted.ParticipantModelFactory
import com.tsarsprocket.reportmid.lol.model.Region
import com.tsarsprocket.reportmid.riotapi.matchV5.ParticipantDto
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class TeamModel @AssistedInject constructor(
    @Assisted val match: MatchModel,
    @Assisted participantDtos: List<ParticipantDto>,
    @Assisted region: Region,
    private val participantModelFactory: ParticipantModelFactory,
    ) {
    enum class TeamColor(val colorCode: Int) { BLUE(100), RED(200) }

    val participants = participantDtos.map { participant -> participantModelFactory.create( this, participant, region ) }
}