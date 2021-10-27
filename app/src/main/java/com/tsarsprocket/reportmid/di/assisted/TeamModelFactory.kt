package com.tsarsprocket.reportmid.di.assisted

import com.tsarsprocket.reportmid.model.MatchModel
import com.tsarsprocket.reportmid.model.RegionModel
import com.tsarsprocket.reportmid.model.TeamModel
import com.tsarsprocket.reportmid.riotapi.matchV4.ParticipantDto
import com.tsarsprocket.reportmid.riotapi.matchV4.ParticipantIdentityDto
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory

@AssistedFactory
interface TeamModelFactory {
    fun create( match: MatchModel, lstMemberDtos: List<Pair<ParticipantDto, ParticipantIdentityDto>>, region: RegionModel ): TeamModel
}