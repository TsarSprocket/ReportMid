package com.tsarsprocket.reportmid.di.assisted

import com.tsarsprocket.reportmid.model.CurrentMatchTeamModel
import com.tsarsprocket.reportmid.model.RegionModel
import com.tsarsprocket.reportmid.riotapi.spectatorV4.CurrentGameParticipant
import com.tsarsprocket.reportmid.riotapi.spectatorV4.Team
import dagger.assisted.AssistedFactory

@AssistedFactory
interface CurrentMatchTeamModelFactory {
    fun create( col: Team, participants: List<CurrentGameParticipant>, region: RegionModel ): CurrentMatchTeamModel
}