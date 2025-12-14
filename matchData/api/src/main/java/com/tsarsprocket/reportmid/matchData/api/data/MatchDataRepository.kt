package com.tsarsprocket.reportmid.matchData.api.data

import com.tsarsprocket.reportmid.lol.api.domain.model.Region
import com.tsarsprocket.reportmid.matchData.api.data.model.Match
import com.tsarsprocket.reportmid.matchData.api.data.model.MatchNotFoundException
import com.tsarsprocket.reportmid.matchData.api.data.model.MatchWithMeta

interface MatchDataRepository {

    /**
     * Retrieves a match from a player's match history by its position. Additional information is provided along with the match data like hint about next record availability
     *
     * @param puuid The player's unique identifier
     * @param region The region where the match was played
     * @param position The position of the match in the player's match history
     * @return A [MatchWithMeta] object containing the match data and metadata
     * @throws MatchNotFoundException if the match is not found
     */
    @Throws(MatchNotFoundException::class)
    suspend fun getMatch(puuid: String, region: Region, position: Int): MatchWithMeta

    /**
     * Retrieves a specific match by its ID.
     *
     * @param matchId The ID of the match
     * @param region The region where the match was played
     * @return A [Match] object containing the match data
     * @throws MatchNotFoundException if the match is not found
     */
    @Throws(MatchNotFoundException::class)
    suspend fun getMatch(matchId: String, region: Region): Match
}
