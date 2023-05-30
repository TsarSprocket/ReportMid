package com.tsarsprocket.reportmid.summoner.model

import com.tsarsprocket.reportmid.lol.model.PuuidAndRegion
import com.tsarsprocket.reportmid.lol.model.Region
import io.reactivex.Observable
import io.reactivex.Single

interface SummonerRepository {
    fun getByAccountId(accountId: String, region: Region): Single<SummonerModel>
    fun getByPuuidAndRegion(puuidAndRegion: PuuidAndRegion): Single<SummonerModel>
    fun getBySummonerId(summonerId: String, region: Region): Single<SummonerModel>
    fun getBySummonerName(summonerName: String, region: Region): Single<SummonerModel>
    fun getMine(): Observable<List<SummonerModel>>
    fun getMineForRegionSelected(reg: Region): Observable<List<Pair<SummonerModel, Boolean>>>
}