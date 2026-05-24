package com.tsarsprocket.reportmid.matchUpView.impl.reducer

import com.tsarsprocket.reportmid.lol.api.domain.model.Region
import com.tsarsprocket.reportmid.matchUpView.impl.domain.model.CurrentMatchUp
import com.tsarsprocket.reportmid.matchUpView.impl.domain.model.Team
import com.tsarsprocket.reportmid.matchUpView.impl.viewState.MatchUpState
import com.tsarsprocket.reportmid.matchUpView.impl.viewState.TeamInfo
import javax.inject.Inject

internal class Mapper @Inject constructor() {

    fun map(from: CurrentMatchUp, puuid: String, region: Region): MatchUpState {
        return MatchUpState(
            puuid = puuid,
            region = region,
            teams = from.teams.map { mapTeam(it) },
        )
    }

    private fun mapTeam(from: Team): TeamInfo {
        return TeamInfo(
            isBlueSide = from.id == BLUE_TEAM_ID,
            // TODO: Map team fields
        )
    }

    private companion object {
        const val BLUE_TEAM_ID = 100
    }
}
