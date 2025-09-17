package com.tsarsprocket.reportmid.matchData.api.data

import com.tsarsprocket.reportmid.lol.api.model.Region
import com.tsarsprocket.reportmid.matchData.api.data.model.Match
import com.tsarsprocket.reportmid.matchData.api.data.model.MatchNotFoundException

interface MatchDataRepository {
    @Throws(MatchNotFoundException::class)
    suspend fun getMatch(puuid: String, region: Region, position: Int): Match
}