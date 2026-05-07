package com.tsarsprocket.reportmid.currentGameData.api.data

import com.tsarsprocket.reportmid.currentGameData.api.data.model.CurrentGame
import com.tsarsprocket.reportmid.lol.api.domain.model.Region

interface CurrentGameDataRepository {
    suspend fun getCurrentGameData(puuid: String, region: Region): CurrentGame
}