package com.tsarsprocket.reportmid.di.assisted

import com.tsarsprocket.reportmid.model.ParticipantModel
import com.tsarsprocket.reportmid.model.RegionModel
import com.tsarsprocket.reportmid.model.TeamModel
import com.tsarsprocket.reportmid.riotapi.matchV4.ParticipantDto
import com.tsarsprocket.reportmid.riotapi.matchV4.ParticipantIdentityDto
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory

@AssistedFactory
interface ParticipantModelFactory {
    fun create( team: TeamModel, dto: ParticipantDto, identityDto: ParticipantIdentityDto, region: RegionModel ): ParticipantModel
}