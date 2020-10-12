package com.tsarsprocket.reportmid.model.state

import com.tsarsprocket.reportmid.model.Repository
import com.tsarsprocket.reportmid.model.SummonerModel
import io.reactivex.Single

class MyFriendModel(val repository: Repository, val id: Long) {

    val summoner: Single<SummonerModel> by lazy { repository.getSummonerForFriend(this).cache() }
}