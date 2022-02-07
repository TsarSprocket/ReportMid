package com.tsarsprocket.reportmid.di.assisted

import com.tsarsprocket.reportmid.model.ParticipantModel
import com.tsarsprocket.reportmid.model.RegionModel
import com.tsarsprocket.reportmid.model.TeamModel
import com.tsarsprocket.reportmid.riotapi.matchV5.ParticipantDto
import com.tsarsprocket.reportmid.riotapi.matchV5.ParticipantIdentityDto
import dagger.assisted.AssistedFactory

@AssistedFactory
interface ParticipantModelFactory {
    fun create( team: TeamModel, dto: ParticipantDto, region: RegionModel ): ParticipantModel
}