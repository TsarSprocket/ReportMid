package com.tsarsprocket.reportmid.viewmodel

import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tsarsprocket.reportmid.RIOTIconProvider
import com.tsarsprocket.reportmid.lol.model.PuuidAndRegion
import com.tsarsprocket.reportmid.model.Repository
import com.tsarsprocket.reportmid.model.my_friend.MyFriendModel
import com.tsarsprocket.reportmid.summoner_api.model.MyAccount
import com.tsarsprocket.reportmid.summoner_api.model.Summoner
import com.tsarsprocket.reportmid.tools.toLiveData
import com.tsarsprocket.reportmid.tools.toObservable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.ReplaySubject
import javax.inject.Inject

class ManageFriendsViewModel @Inject constructor(
    private val repository: Repository,
    private val iconProvider: RIOTIconProvider,
) : ViewModel() {

    //  Input  ////////////////////////////////////////////////////////////////

    val selectedAccPositionLive = MutableLiveData<Int>()
    val checkedItemsLive = MutableLiveData<Set<IndexedValue<FriendListItem>>>(setOf())

    //  Rx  ///////////////////////////////////////////////////////////////////

    private val selectedAccPositionObservable: Observable<Int> = selectedAccPositionLive.toObservable()

    private val myAccsAndSumsObservable: Observable<List<Triple<MyAccount, Summoner, Drawable>>> = repository.getMyAccounts()
        .map { lst -> lst.map { myAcc -> repository.getSummonerForMyAccount(myAcc).blockingGet().let { Triple(myAcc, it, iconProvider.getProfileIcon(it.iconId).blockingGet()) } } }

    private val selectedAccAndSum: ReplaySubject<Triple<MyAccount, Summoner, Drawable>> =
        ReplaySubject.create<Triple<MyAccount, Summoner, Drawable>>(1).also { subj ->
            Observable.combineLatest(selectedAccPositionObservable, myAccsAndSumsObservable) { pos, tri ->
                if(pos < tri.size) Maybe.just(tri[pos])
                else Maybe.empty()
            }
                .filter { maybe -> !maybe.isEmpty.blockingGet() }
                .map { maybe ->
                    maybe.blockingGet()
                }
                .subscribe(subj)
        }

    private val selectedSummonerObservable: Observable<Summoner> = selectedAccAndSum
        .map {
            it.second
        }

    private val friendSummonersObservable: Observable<List<FriendListItem>> = selectedAccAndSum
        .switchMap {
            repository.getFriendsForAcc(it.first)
        }
        .map { lst -> lst.map { friend -> friend to friend.summoner } }
        .map { lst ->
            lst.map { (friend, sumObs) ->
                val sum = sumObs.blockingGet()
                FriendListItem(friend, sum, iconProvider.getProfileIcon(sum.iconId).blockingGet())
            }
        }

    //  Output  ///////////////////////////////////////////////////////////////

    val myAccsAndSumsLive: LiveData<List<Triple<MyAccount, Summoner, Drawable>>> = myAccsAndSumsObservable.toLiveData()
    val selectedSummonerLive: LiveData<Summoner> = selectedSummonerObservable.toLiveData()
    val friendSummonersLive: LiveData<List<FriendListItem>> = friendSummonersObservable.toLiveData()

    //  Helper  ///////////////////////////////////////////////////////////////

    val disposer = CompositeDisposable()

    //  Init  /////////////////////////////////////////////////////////////////

    fun init(myAccInitPos: Int) {
        if(selectedAccPositionLive.value == null) selectedAccPositionLive.value = myAccInitPos
    }

    fun createFriend(friendsPuuidAndRegion: PuuidAndRegion, mySumPuuidAndRegion: PuuidAndRegion) {
        repository.createFriend(friendsPuuidAndRegion, mySumPuuidAndRegion)
    }

    fun deleteSelectedFriends() {
        checkedItemsLive.value?.let { set ->
            repository.deleteFriendsAndSummoners(set.map { idxVal -> idxVal.value.friend })
        }
    }

    //  Classes  //////////////////////////////////////////////////////////////

    data class FriendListItem(
        val friend: MyFriendModel,
        val sum: Summoner,
        val icon: Drawable,
        var isChecked: Boolean = false
    )
}
