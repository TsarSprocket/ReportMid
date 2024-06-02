package com.tsarsprocket.reportmid.summonerApi.di

import com.tsarsprocket.reportmid.summonerApi.data.SummonerRepository

interface SummonerApi {
    fun getSummonerRepository(): SummonerRepository
}