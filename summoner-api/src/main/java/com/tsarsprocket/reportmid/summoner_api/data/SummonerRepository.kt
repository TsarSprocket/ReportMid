package com.tsarsprocket.reportmid.summoner_api.data

import com.tsarsprocket.reportmid.lol.model.Puuid
import com.tsarsprocket.reportmid.lol.model.PuuidAndRegion
import com.tsarsprocket.reportmid.lol.model.Region
import com.tsarsprocket.reportmid.summoner_api.model.MyAccount
import com.tsarsprocket.reportmid.summoner_api.model.Summoner
import com.tsarsprocket.reportmid.summoner_api.model.SummonerInfo

interface SummonerRepository {
    suspend fun addKnownSummoner(puuid: Puuid, region: Region): SummonerInfo
    suspend fun forgetSummonerById(id: Long)
    suspend fun getKnownSummonerId(puuidAndRegion: PuuidAndRegion): Long
    suspend fun getMySummoners(): List<Summoner>
    suspend fun getMySummonersForRegion(reg: Region): List<Summoner>
    suspend fun getSummonerInfoById(id: Long): SummonerInfo
    suspend fun isSummonerKnown(puuidAndRegion: PuuidAndRegion): Boolean
    suspend fun requestRemoteSummonerByName(name: String, region: Region): Summoner
    suspend fun requestRemoteSummonerByPuuidAndRegion(puuidAndRegion: PuuidAndRegion): Summoner
    suspend fun requestRemoteSummonerByRiotId(id: String, region: Region): Summoner

    suspend fun createMyAccount(summonerId: Long): MyAccount
    suspend fun deleteMyAccount(myAccount: MyAccount)
    suspend fun getAllMyAccounts(): List<MyAccount>
    suspend fun getMyAccountById(id: Long): MyAccount
    suspend fun getMyAccountByPuuidAndRegion(puuidAndRegion: PuuidAndRegion): MyAccount
    suspend fun getMyAccountBySummonerId(summonerId: Long): MyAccount
    suspend fun getMyAccountsByRegion(region: Region): List<MyAccount>
    suspend fun getNumberOfMyAccounts(): Int
    suspend fun requestSummonerByMyAccount(myAccount: MyAccount): Summoner
}