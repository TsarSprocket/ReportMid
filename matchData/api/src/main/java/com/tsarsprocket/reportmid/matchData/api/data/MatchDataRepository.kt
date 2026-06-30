package com.tsarsprocket.reportmid.matchData.api.data

import com.tsarsprocket.reportmid.lol.api.domain.model.Region
import com.tsarsprocket.reportmid.matchData.api.data.model.Match
import com.tsarsprocket.reportmid.matchData.api.data.model.MatchWithMeta

interface MatchDataRepository {

    suspend fun getMatch(puuid: String, region: Region, position: Int): MatchWithMeta

    suspend fun getMatch(matchId: String, region: Region): Match
}
