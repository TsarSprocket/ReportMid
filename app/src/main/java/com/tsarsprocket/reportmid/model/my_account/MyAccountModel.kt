package com.tsarsprocket.reportmid.model.my_account

import com.tsarsprocket.reportmid.model.Repository
import com.tsarsprocket.reportmid.model.my_friend.MyFriendModel
import com.tsarsprocket.reportmid.summoner.model.SummonerModel
import io.reactivex.Single
import io.reactivex.subjects.ReplaySubject

class MyAccountModel(val repository: Repository, val id: Long) {

    val summoner: Single<SummonerModel> by lazy { repository.getSummonerForMyAccount(this) }
    val friends: ReplaySubject<List<MyFriendModel>> by lazy { repository.getFriendsForAcc(this) }

    fun activate() {
        repository.activateAccount(this)
    }
}