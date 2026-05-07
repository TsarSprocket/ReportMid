package com.tsarsprocket.reportmid.currentGameData.api.di

import com.tsarsprocket.reportmid.currentGameData.api.data.CurrentGameDataRepository

interface CurrentGameDataApi {
    fun getCurrentGameDataRepository(): CurrentGameDataRepository
}