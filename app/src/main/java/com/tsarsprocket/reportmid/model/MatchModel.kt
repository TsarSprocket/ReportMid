package com.tsarsprocket.reportmid.model

import android.util.SparseArray
import com.merakianalytics.orianna.types.common.GameMode
import com.merakianalytics.orianna.types.common.GameType
import com.merakianalytics.orianna.types.common.Queue
import com.merakianalytics.orianna.types.core.match.Match
import com.tsarsprocket.reportmid.di.assisted.TeamModelFactory
import com.tsarsprocket.reportmid.riotapi.matchV4.MatchDto
import com.tsarsprocket.reportmid.riotapi.matchV4.ParticipantDto
import com.tsarsprocket.reportmid.riotapi.matchV4.ParticipantIdentityDto
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.lang.IllegalArgumentException

class MatchModel @AssistedInject constructor(
    @Assisted matchDto: MatchDto,
    @Assisted region: RegionModel,
    private val teamModelFactory: TeamModelFactory,
) {
    val id = matchDto.gameId
    val blueTeam: TeamModel // by lazy { repository.getTeamModel( shadowMatch.blueTeam ).replay( 1 ).autoConnect() }
    val redTeam: TeamModel // by lazy { repository.getTeamModel( shadowMatch.redTeam ).replay( 1 ).autoConnect() }
    val teams: Array<TeamModel>
    val gameType = try{ Repository.getGameType( GameType.valueOf(matchDto.gameType), Queue.withId(matchDto.queueId), GameMode.valueOf(matchDto.gameMode), GameMap.withId(matchDto.mapId) ) } catch (ex: IllegalArgumentException ) { GameTypeModel.UNKNOWN }
    val remake = matchDto.gameDuration < 10 * 60 // i.e. game duration < 10 min

    init {
        val mapIdentities = matchDto.participantIdentities.map { it.participantId to it }.toMap()
        blueTeam = teamModelFactory.create( this, filterTemDtos(TeamModel.TeamColor.BLUE, matchDto.participants, mapIdentities), region)
        redTeam = teamModelFactory.create( this, filterTemDtos(TeamModel.TeamColor.RED, matchDto.participants, mapIdentities), region)
        teams = arrayOf( blueTeam, redTeam )
    }

    private fun filterTemDtos(teamColor: TeamModel.TeamColor, allParticipants: List<ParticipantDto>, mapIdentities: Map<Int,ParticipantIdentityDto>): List<Pair<ParticipantDto,ParticipantIdentityDto>> =
        allParticipants.filter { it.teamId == teamColor.colorCode }.map { it to mapIdentities[ it.participantId ]!! }
}