package com.tsarsprocket.reportmid.di.assisted

import com.tsarsprocket.reportmid.lol.model.Region
import com.tsarsprocket.reportmid.model.MatchModel
import com.tsarsprocket.reportmid.model.TeamModel
import com.tsarsprocket.reportmid.riotapi.matchV5.ParticipantDto
import dagger.assisted.AssistedFactory

@AssistedFactory
interface TeamModelFactory {
    fun create(match: MatchModel, lstMemberDtos: List<ParticipantDto>, region: Region): TeamModel
}