package com.tsarsprocket.reportmid.di.assisted

import com.tsarsprocket.reportmid.model.PlayerModel
import com.tsarsprocket.reportmid.model.RegionModel
import com.tsarsprocket.reportmid.riotapi.spectatorV4.CurrentGameParticipant
import dagger.assisted.AssistedFactory

@AssistedFactory
interface PlayerModelFactory {
    fun create( info: CurrentGameParticipant, region: RegionModel ): PlayerModel
}