package com.tsarsprocket.reportmid.model

import com.merakianalytics.orianna.types.common.GameMode
import com.merakianalytics.orianna.types.common.GameType
import com.merakianalytics.orianna.types.common.Queue
import com.tsarsprocket.reportmid.di.assisted.TeamModelFactory
import com.tsarsprocket.reportmid.riotapi.matchV5.MatchDto
import com.tsarsprocket.reportmid.riotapi.matchV5.ParticipantDto
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.lang.IllegalArgumentException

class MatchModel @AssistedInject constructor(
    @Assisted matchDto: MatchDto,
    @Assisted region: RegionModel,
    private val teamModelFactory: TeamModelFactory,
) {
    val id = matchDto.metadata.matchId
    val blueTeam: TeamModel // by lazy { repository.getTeamModel( shadowMatch.blueTeam ).replay( 1 ).autoConnect() }
    val redTeam: TeamModel // by lazy { repository.getTeamModel( shadowMatch.redTeam ).replay( 1 ).autoConnect() }
    val teams: Array<TeamModel>
    val gameType = try{ Repository.getGameType( GameType.valueOf(matchDto.info.gameType), Queue.withId(matchDto.info.queueId),
        GameMode.valueOf(matchDto.info.gameMode), GameMap.withId(matchDto.info.mapId) ) } catch (ex: IllegalArgumentException ) { GameTypeModel.UNKNOWN }
    val remake = matchDto.info.gameDuration < 10 * 60 // i.e. game duration < 10 min

    init {
        blueTeam = teamModelFactory.create( this, filterTemDtos(TeamModel.TeamColor.BLUE, matchDto.info.participants), region)
        redTeam = teamModelFactory.create( this, filterTemDtos(TeamModel.TeamColor.RED, matchDto.info.participants), region)
        teams = arrayOf( blueTeam, redTeam )
    }

    private fun filterTemDtos(teamColor: TeamModel.TeamColor, allParticipants: List<ParticipantDto>): List<ParticipantDto> =
        allParticipants.filter { it.teamId == teamColor.colorCode }
}