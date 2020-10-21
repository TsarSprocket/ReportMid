package com.tsarsprocket.reportmid.viewmodel

import android.graphics.Bitmap
import android.view.View
import android.widget.LinearLayout
import androidx.lifecycle.*
import com.tsarsprocket.reportmid.model.RegionModel
import com.tsarsprocket.reportmid.model.Repository
import com.tsarsprocket.reportmid.model.SummonerModel
import com.tsarsprocket.reportmid.model.state.MyAccountModel
import com.tsarsprocket.reportmid.model.state.MyFriendModel
import com.tsarsprocket.reportmid.tools.OneTimeObserver
import com.tsarsprocket.reportmid.tools.toLiveData
import com.tsarsprocket.reportmid.tools.toObservable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DrawerViewModel @Inject constructor(val repository: Repository) : ViewModel() {

    //  LiveData Input  ///////////////////////////////////////////////////////

    val selectedRegionPositionLive = MutableLiveData<Int>()  // In from spinner

    //  Rx  ///////////////////////////////////////////////////////////////////

    private val selectedRegionPositionObservable: Observable<Int> = selectedRegionPositionLive.toObservable()

    private val currentRegionsObservable: Observable<List<RegionModel>> = repository.getMyRegions().map { lst -> lst.sortedBy { it.title } }

    private val mySummonersInSelectedRegionObservable: Observable<List<Triple<Bitmap,SummonerModel,Boolean>>> =
        Observable.combineLatest( selectedRegionPositionObservable, currentRegionsObservable ) { pos, lst -> lst[pos] }
            .switchMap { reg -> repository.getMySummonersObservableForRegion(reg) }
            .map { lst ->
                lst.sortedWith { o1, o2 -> SUMO_COMP.compare(o1.first, o2.first) }
                    .map { (sum, isSelected) -> Triple(sum.icon.blockingFirst(), sum, isSelected) }
            }

    private val currentAccountObservable: Observable<List<MyAccountModel>> =
        Observable.combineLatest( selectedRegionPositionObservable, currentRegionsObservable ) { pos, regs -> regs[pos] }
            .switchMap { repository.getCurrentAccountsPerRegionObservable(it) }

    private val myFriendsRx: Observable<List<MyFriendData>> = currentAccountObservable
        .filter { lst -> lst.isNotEmpty() }
        .switchMap { repository.getFriendsForAcc(it.first()) }
        .map { lst -> lst.map { fr -> fr to fr.summoner.blockingGet() } } // Observable.zip(lst.map { friend -> friend.summoner.toObservable().map { sum -> friend to sum } }) { arr -> arr.toList() as List<Pair<MyFriendModel,SummonerModel>> }
        .map { lst -> lst.map { pair -> MyFriendData(pair.first, pair.second, pair.second.icon.blockingFirst(), pair.second.name) } } // Observable.zip(lst.map { (friend, sum) -> sum.icon.map { bmp -> MyFriendData(friend, sum, bmp, sum.name) } }) { arr -> arr.toList() as List<MyFriendData> }

    //  LiveData Output  //////////////////////////////////////////////////////

    val currentRegionsLive: LiveData<List<RegionModel>> = currentRegionsObservable.toLiveData()
    val mySummonersInSelectedRegionLive: LiveData<List<Triple<Bitmap,SummonerModel,Boolean>>> = mySummonersInSelectedRegionObservable.toLiveData()
    val currentAccountLive: LiveData<List<MyAccountModel>> = currentAccountObservable.toLiveData()
    val myFriendsLive: LiveData<List<MyFriendData>> = myFriendsRx.toLiveData()

    //  Utility  //////////////////////////////////////////////////////////////

    private val disposer = CompositeDisposable()

    //  Init  /////////////////////////////////////////////////////////////////

    init {

        disposer.add(
            repository.getSelectedAccountRegion()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { selReg ->
                    object : OneTimeObserver<List<RegionModel>>(){
                        override fun onOneTimeChanged(lstRegs: List<RegionModel>) {
                            selectedRegionPositionLive.value = lstRegs.indexOf(selReg)
                        }
                    }.observeForeverOn(currentRegionsLive)
                }
        )
    }

    //  Methods  //////////////////////////////////////////////////////////////

    private fun getCurrentRegionsValue() = currentRegionsLive.value

    fun activateAcc(sum: SummonerModel, view: View, group: LinearLayout) {
        disposer.add(sum.myAccount
            .observeOn(Schedulers.io())
            .subscribe {
                it.activate()
            })
    }

    override fun onCleared() {
        disposer.clear()
    }

    //  Static  ///////////////////////////////////////////////////////////////

    companion object {
        val SUMO_COMP = SummonerModel.ByNameAndRegionComparator()
    }

    //  Classes  //////////////////////////////////////////////////////////////

    data class MyFriendData(
        val friend: MyFriendModel,
        val sum: SummonerModel,
        val icon: Bitmap,
        val name: String,
    )
}
