package com.tsarsprocket.reportmid.summonerApi.data

import com.tsarsprocket.reportmid.lol.model.GameName
import com.tsarsprocket.reportmid.lol.model.Puuid
import com.tsarsprocket.reportmid.lol.model.PuuidAndRegion
import com.tsarsprocket.reportmid.lol.model.Region
import com.tsarsprocket.reportmid.lol.model.TagLine
import com.tsarsprocket.reportmid.summonerApi.model.Friend
import com.tsarsprocket.reportmid.summonerApi.model.MyAccount
import com.tsarsprocket.reportmid.summonerApi.model.RiotAccount
import com.tsarsprocket.reportmid.summonerApi.model.Summoner
import com.tsarsprocket.reportmid.summonerApi.model.SummonerInfo

interface SummonerRepository {
    suspend fun getRiotAccount(gameName: GameName, tagLine: TagLine, region: Region): RiotAccount

    suspend fun getKnownSummonerId(puuidAndRegion: PuuidAndRegion): Long
    suspend fun getMySummoners(): List<Summoner>
    suspend fun getMySummonersForRegion(reg: Region): List<Summoner>
    suspend fun getSummonerInfoById(id: Long): SummonerInfo
    suspend fun isSummonerKnown(puuidAndRegion: PuuidAndRegion): Boolean
    suspend fun requestRemoteSummonerByName(name: String, region: Region): Summoner
    suspend fun requestRemoteSummonerByGameNameAndTagLine(gameName: GameName, tagLine: TagLine, region: Region): Summoner
    suspend fun requestRemoteSummonerByPuuidAndRegion(puuidAndRegion: PuuidAndRegion): Summoner
    suspend fun requestRemoteSummonerByRiotId(id: String, region: Region): Summoner

    suspend fun createMyAccount(puuid: Puuid, region: Region): MyAccount
    suspend fun deleteMyAccount(myAccount: MyAccount)
    suspend fun getAllMyAccounts(): List<MyAccount>
    suspend fun getMyAccountById(id: Long): MyAccount
    suspend fun getMyAccountByPuuidAndRegion(puuidAndRegion: PuuidAndRegion): MyAccount
    suspend fun getMyAccountBySummonerId(summonerId: Long): MyAccount
    suspend fun getMyAccountsByRegion(region: Region): List<MyAccount>
    suspend fun getNumberOfMyAccounts(): Int
    suspend fun requestSummonerByMyAccount(myAccount: MyAccount): Summoner

    suspend fun createFriend(myAccount: MyAccount, puuid: Puuid, region: Region): Friend
    suspend fun getFriends(myAccount: MyAccount): List<Friend>
    suspend fun requestSummonerForFriend(friend: Friend): Summoner
    suspend fun deleteFriend(friend: Friend)
}