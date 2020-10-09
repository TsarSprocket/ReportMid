package com.tsarsprocket.reportmid.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tsarsprocket.reportmid.model.PuuidAndRegion
import com.tsarsprocket.reportmid.model.Repository
import com.tsarsprocket.reportmid.model.SummonerModel
import com.tsarsprocket.reportmid.model.state.MyAccountModel
import com.tsarsprocket.reportmid.tools.toLiveData
import com.tsarsprocket.reportmid.tools.toObservable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ManageFriendsViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    //  Input  ////////////////////////////////////////////////////////////////

    val selectedAccPositionLive = MutableLiveData<Int>()

    //  Rx  ///////////////////////////////////////////////////////////////////

    val selectedAccPositionObservable: Observable<Int> = selectedAccPositionLive.toObservable()
    private val myAccsAndSumsObservable: Observable<List<Triple<MyAccountModel, SummonerModel, Bitmap>>> = repository.getMyAccounts()
        .map { lst ->
            lst.mapNotNull { myAcc ->
                if (!myAcc.summoner.isEmpty.blockingGet()) {
                    myAcc.summoner.blockingGet().let { Triple(myAcc, it, it.icon.blockingFirst()) }
                } else null
            }
        }

    private val selectedSummonerObservable: Observable<SummonerModel> =
        Observable.combineLatest(selectedAccPositionObservable,myAccsAndSumsObservable) { pos, tri ->
            if(pos < tri.size) Maybe.just(tri[pos].second)
            else Maybe.empty()
        }
            .filter { !it.isEmpty.blockingGet() }
            .map { it.blockingGet() }

    //  Output  ///////////////////////////////////////////////////////////////

    val myAccsAndSumsLive: LiveData<List<Triple<MyAccountModel,SummonerModel,Bitmap>>> = myAccsAndSumsObservable.toLiveData()
    val selectedSummonerLive: LiveData<SummonerModel> = selectedSummonerObservable.toLiveData()

    //  Helper  ///////////////////////////////////////////////////////////////

    val disposer = CompositeDisposable()

    //  Init  /////////////////////////////////////////////////////////////////

    fun init(myAccInitPos: Int) {
        if (selectedAccPositionLive.value == null) selectedAccPositionLive.value = myAccInitPos
    }

    fun createFriend(friendsPuuidAndRegion: PuuidAndRegion, mySumPuuidAndRegion: PuuidAndRegion) {
        repository.createFriend(friendsPuuidAndRegion, mySumPuuidAndRegion)
    }
}
