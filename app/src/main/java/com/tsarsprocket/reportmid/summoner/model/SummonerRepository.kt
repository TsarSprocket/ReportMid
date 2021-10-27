package com.tsarsprocket.reportmid.summoner.model

import com.tsarsprocket.reportmid.model.PuuidAndRegion
import com.tsarsprocket.reportmid.model.RegionModel
import io.reactivex.Observable
import io.reactivex.Single

interface SummonerRepository {
    fun getByAccountId(accountId: String, region: RegionModel): Single<SummonerModel>
    fun getByPuuidAndRegion(puuidAndRegion: PuuidAndRegion): Single<SummonerModel>
    fun getBySummonerId(summonerId: String, region: RegionModel): Single<SummonerModel>
    fun getBySummonerName(summonerName: String, region: RegionModel): Single<SummonerModel>
    fun getMine(): Observable<List<SummonerModel>>
    fun getMineForRegionSelected(reg: RegionModel): Observable<List<Pair<SummonerModel, Boolean>>>
}