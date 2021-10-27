package com.tsarsprocket.reportmid.model

import com.tsarsprocket.reportmid.di.assisted.PlayerModelFactory
import com.tsarsprocket.reportmid.riotapi.spectatorV4.CurrentGameParticipant
import com.tsarsprocket.reportmid.riotapi.spectatorV4.Team
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class CurrentMatchTeamModel @AssistedInject constructor(
    @Assisted col: Team,
    @Assisted participants: List<CurrentGameParticipant>,
    @Assisted region: RegionModel,
    private val playerModelFactory: PlayerModelFactory,
) {
    val color = col
    val participants: List<PlayerModel> = participants.map{ playerModelFactory.create( it, region ) }
}