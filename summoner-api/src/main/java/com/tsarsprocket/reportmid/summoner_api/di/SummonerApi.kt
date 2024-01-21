package com.tsarsprocket.reportmid.summoner_api.di

import com.tsarsprocket.reportmid.summoner_api.data.SummonerRepository

interface SummonerApi {
    fun getSummonerRepository(): SummonerRepository
}