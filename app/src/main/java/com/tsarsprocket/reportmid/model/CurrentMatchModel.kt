package com.tsarsprocket.reportmid.model

import com.merakianalytics.orianna.types.common.GameMode
import com.merakianalytics.orianna.types.common.GameType
import com.tsarsprocket.reportmid.di.assisted.CurrentMatchTeamModelFactory
import com.tsarsprocket.reportmid.summoner.model.SummonerModel
import com.merakianalytics.orianna.types.common.Map as OriannaMap
import com.merakianalytics.orianna.types.common.Queue as OriannaQueue
import com.tsarsprocket.reportmid.riotapi.spectatorV4.CurrentGameParticipant
import com.tsarsprocket.reportmid.riotapi.spectatorV4.SpectatorV4Service
import com.tsarsprocket.reportmid.riotapi.spectatorV4.Team
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.*

class CurrentMatchModel @AssistedInject constructor(
    @Assisted summoner: SummonerModel,
    private val repository: Repository,
    private val currentMatchTeamModelFactory: CurrentMatchTeamModelFactory,
) {

    val blueTeam: CurrentMatchTeamModel
    val redTeam: CurrentMatchTeamModel
    val creationMoment: Calendar
    val gameType: GameTypeModel

    init {
        val info = repository.retrofitServiceProvider.getService(summoner.region, SpectatorV4Service::class.java).spectatorV4(summoner.id).blockingFirst()
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