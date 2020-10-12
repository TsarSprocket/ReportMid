package com.tsarsprocket.reportmid.model.state

import com.tsarsprocket.reportmid.model.Repository
import com.tsarsprocket.reportmid.model.SummonerModel
import io.reactivex.Maybe
import io.reactivex.subjects.ReplaySubject

class MyAccountModel(val repository: Repository, val id: Long) {

    val summoner: Maybe<SummonerModel> by lazy { repository.getSummonerForMyAccount(this) }
    val friends: ReplaySubject<List<MyFriendModel>> by lazy { repository.getFriendsForAcc(this) }

    fun activate() {
        repository.activateAccount(this)
    }
}