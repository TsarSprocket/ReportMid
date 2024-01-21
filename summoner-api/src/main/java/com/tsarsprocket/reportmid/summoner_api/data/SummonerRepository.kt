package com.tsarsprocket.reportmid.summoner_api.data

import com.tsarsprocket.reportmid.lol.model.PuuidAndRegion
import com.tsarsprocket.reportmid.lol.model.Region
import com.tsarsprocket.reportmid.summoner_api.model.SummonerModel

interface SummonerRepository {
    fun getByAccountId(accountId: String, region: Region): io.reactivex.Single<SummonerModel>
    fun getByPuuidAndRegion(puuidAndRegion: PuuidAndRegion): io.reactivex.Single<SummonerModel>
    fun getBySummonerId(summonerId: String, region: Region): io.reactivex.Single<SummonerModel>
    fun getBySummonerName(summonerName: String, region: Region): io.reactivex.Single<SummonerModel>
    fun getMine(): io.reactivex.Observable<List<SummonerModel>>
    fun getMineForRegionSelected(reg: Region): io.reactivex.Observable<List<Pair<SummonerModel, Boolean>>>
}