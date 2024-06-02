package com.tsarsprocket.reportmid.leaguePositionImpl.data

import com.tsarsprocket.reportmid.baseApi.common.Mapper
import com.tsarsprocket.reportmid.leaguePositionApi.model.LeaguePosition
import com.tsarsprocket.reportmid.leaguePositionImpl.retrofit.LeaguePositionDto
import com.tsarsprocket.reportmid.lol.model.Division
import com.tsarsprocket.reportmid.lol.model.QueueType
import com.tsarsprocket.reportmid.lol.model.Tier
import com.tsarsprocket.reportmid.utils.common.orZero
import javax.inject.Inject

class LeaguePositionMapper @Inject constructor() : Mapper<LeaguePositionDto, LeaguePosition> {

    override operator fun invoke(from: LeaguePositionDto): LeaguePosition {
        return LeaguePosition(
            queueType = QueueType.byName(from.queueType.orEmpty()),
            tier = Tier.byName(from.tier.orEmpty()),
            division = Division.byRoman(from.rank.orEmpty()),
            wins = from.wins.orZero(),
            losses = from.losses.orZero(),
        )
    }
}