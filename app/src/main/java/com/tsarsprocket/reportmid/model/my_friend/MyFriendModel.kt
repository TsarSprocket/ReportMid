package com.tsarsprocket.reportmid.model.my_friend

import com.tsarsprocket.reportmid.model.Repository
import com.tsarsprocket.reportmid.summoner_api.model.Summoner
import io.reactivex.Single

class MyFriendModel(val repository: Repository, val id: Long) {

    val summoner: Single<Summoner> by lazy { repository.getSummonerForFriend(this).cache() }
}