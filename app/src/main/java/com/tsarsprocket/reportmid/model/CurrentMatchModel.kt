package com.tsarsprocket.reportmid.model

import com.merakianalytics.orianna.types.common.GameMode
import com.merakianalytics.orianna.types.common.GameType
import com.tsarsprocket.reportmid.di.assisted.CurrentMatchTeamModelFactory
import com.tsarsprocket.reportmid.lol_services_api.riotapi.getService
import com.tsarsprocket.reportmid.riotapi.spectatorV4.CurrentGameParticipant
import com.tsarsprocket.reportmid.riotapi.spectatorV4.SpectatorV4Service
import com.tsarsprocket.reportmid.riotapi.spectatorV4.Team
import com.tsarsprocket.reportmid.summoner_api.model.Summoner
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.Calendar
import java.util.LinkedList
import com.merakianalytics.orianna.types.common.Map as OriannaMap
import com.merakianalytics.orianna.types.common.Queue as OriannaQueue

class CurrentMatchModel @AssistedInject constructor(
    @Assisted summoner: Summoner,
    private val repository: Repository,
    private val currentMatchTeamModelFactory: CurrentMatchTeamModelFactory,
) {

    val blueTeam: CurrentMatchTeamModel
    val redTeam: CurrentMatchTeamModel
    val creationMoment: Calendar
    val gameType: GameTypeModel

    init {
        val info = repository.serviceFactory.getService<SpectatorV4Service>(summoner.region).spectatorV4(summoner.riotId).blockingFirst()
        val blue = LinkedList<CurrentGameParticipant>()
        val red = LinkedList<CurrentGameParticipant>()
        info.participants.forEach { (if (it.teamId == Team.Blue.teamId.toLong()) blue else red).add(it) }
        blueTeam = currentMatchTeamModelFactory.create(Team.Blue, blue, summoner.region)
        redTeam = currentMatchTeamModelFactory.create(Team.Red, red, summoner.region)
        gameType = GameTypeModel.by( GameType.valueOf(info.gameType), OriannaQueue.withId(info.gameQueueConfigId.toInt()),
            GameMode.valueOf(info.gameMode), OriannaMap.withId(info.mapId.toInt()) )
        creationMoment = Calendar.getInstance().apply{ timeInMillis = info.gameStartTime }
    }
}