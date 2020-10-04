package com.tsarsprocket.reportmid.model.state

import com.tsarsprocket.reportmid.model.RegionModel
import com.tsarsprocket.reportmid.model.Repository
import com.tsarsprocket.reportmid.model.SummonerModel
import io.reactivex.subjects.ReplaySubject

class MyAccountModel(val repository: Repository, val id: Long) {

    val summoner: ReplaySubject<SummonerModel> by lazy { repository.getSummonerForMyAccount(this) }
}