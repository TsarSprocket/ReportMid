package com.tsarsprocket.reportmid.viewmodel

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tsarsprocket.reportmid.model.PuuidAndRegion
import com.tsarsprocket.reportmid.model.Repository
import com.tsarsprocket.reportmid.model.SummonerModel
import com.tsarsprocket.reportmid.model.state.MyAccountModel
import com.tsarsprocket.reportmid.model.state.MyFriendModel
import com.tsarsprocket.reportmid.tools.toLiveData
import com.tsarsprocket.reportmid.tools.toObservable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.ReplaySubject
import javax.inject.Inject

class ManageFriendsViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    //  Input  ////////////////////////////////////////////////////////////////

    val selectedAccPositionLive = MutableLiveData<Int>()
    val checkedItemsLive = MutableLiveData<Set<IndexedValue<FriendListItem>>>(setOf())

    //  Rx  ///////////////////////////////////////////////////////////////////

    private val selectedAccPositionObservable: Observable<Int> = selectedAccPositionLive.toObservable()

    private val myAccsAndSumsObservable: Observable<List<Triple<MyAccountModel, SummonerModel, Drawable>>> = repository.getMyAccounts()
        .map { lst ->
            lst.mapNotNull { myAcc ->
                if (!myAcc.summoner.isEmpty.blockingGet()) {
                    myAcc.summoner.blockingGet().let { Triple(myAcc, it, it.icon.blockingGet()) }
                } else null
            }
        }

    private val selectedAccAndSum: ReplaySubject<Triple<MyAccountModel, SummonerModel, Drawable>> =
        ReplaySubject.create<Triple<MyAccountModel, SummonerModel, Drawable>>(1).also { subj ->
            Observable.combineLatest(selectedAccPositionObservable,myAccsAndSumsObservable) { pos, tri ->
                if(pos < tri.size) Maybe.just(tri[pos])
                else Maybe.empty()
            }
                .filter { maybe -> !maybe.isEmpty.blockingGet() }
                .map {
                    maybe -> maybe.blockingGet()
                }
                .subscribe(subj)
        }

    private val selectedSummonerObservable: Observable<SummonerModel> = selectedAccAndSum
        .map {
            it.second
        }

    private val friendSummonersObservable: Observable<List<FriendListItem>> = selectedAccAndSum
        .switchMap {
            it.first.friends
        }
        .map { lst -> lst.map { friend -> friend to friend.summoner } }
        .map { lst -> lst.map { (friend, sumObs) ->
                val sum = sumObs.blockingGet()
                FriendListItem(friend, sum, sum.icon.blockingGet())
            }
        }

    //  Output  ///////////////////////////////////////////////////////////////

    val myAccsAndSumsLive: LiveData<List<Triple<MyAccountModel,SummonerModel,Drawable>>> = myAccsAndSumsObservable.toLiveData()
    val selectedSummonerLive: LiveData<SummonerModel> = selectedSummonerObservable.toLiveData()
    val friendSummonersLive: LiveData<List<FriendListItem>> = friendSummonersObservable.toLiveData()

    //  Helper  ///////////////////////////////////////////////////////////////

    val disposer = CompositeDisposable()

    //  Init  /////////////////////////////////////////////////////////////////

    fun init(myAccInitPos: Int) {
        if (selectedAccPositionLive.value == null) selectedAccPositionLive.value = myAccInitPos
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
        val sum: SummonerModel,
        val icon: Drawable,
        var isChecked: Boolean = false
    )
}
