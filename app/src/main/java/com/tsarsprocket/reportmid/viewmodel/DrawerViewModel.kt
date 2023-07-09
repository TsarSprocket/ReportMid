package com.tsarsprocket.reportmid.viewmodel

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.LinearLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tsarsprocket.reportmid.lol.model.Region
import com.tsarsprocket.reportmid.model.Repository
import com.tsarsprocket.reportmid.model.my_account.MyAccountModel
import com.tsarsprocket.reportmid.model.my_friend.MyFriendModel
import com.tsarsprocket.reportmid.summoner.model.SummonerModel
import com.tsarsprocket.reportmid.summoner.model.SummonerRepository
import com.tsarsprocket.reportmid.tools.OneTimeObserver
import com.tsarsprocket.reportmid.tools.toLiveData
import com.tsarsprocket.reportmid.tools.toObservable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DrawerViewModel @Inject constructor(
    val repository: Repository,
    private val summonerRepository: SummonerRepository,
) : ViewModel() {

    //  LiveData Input  ///////////////////////////////////////////////////////

    val selectedRegionPositionLive = MutableLiveData<Int>()  // In from spinner

    //  Rx  ///////////////////////////////////////////////////////////////////

    private val selectedRegionPositionObservable: Observable<Int> = selectedRegionPositionLive.toObservable()

    private val currentRegionsObservable: Observable<List<Region>> = repository.getMyRegions().map { lst -> lst.sortedBy { it.title } }

    private val mySummonersInSelectedRegionObservable: Observable<List<Triple<Drawable, SummonerModel,Boolean>>> =
        Observable.combineLatest( selectedRegionPositionObservable, currentRegionsObservable ) { pos, lst -> lst[pos] }
            .switchMap { reg -> summonerRepository.getMineForRegionSelected(reg) }
            .map { lst ->
                lst.sortedWith { o1, o2 -> SUMO_COMP.compare(o1.first, o2.first) }
                    .map { (sum, isSelected) -> Triple(sum.icon.blockingGet(), sum, isSelected) }
            }

    private val currentAccountObservable: Observable<List<MyAccountModel>> =
        Observable.combineLatest( selectedRegionPositionObservable, currentRegionsObservable ) { pos, regs -> regs[pos] }
            .switchMap { repository.getCurrentAccountsPerRegionObservable(it) }

    private val myFriendsRx: Observable<List<MyFriendData>> = currentAccountObservable
        .filter { lst -> lst.isNotEmpty() }
        .switchMap { repository.getFriendsForAcc(it.first()) }
        .map { lst -> lst.map { fr -> fr to fr.summoner.blockingGet() } } // Observable.zip(lst.map { friend -> friend.summoner.toObservable().map { sum -> friend to sum } }) { arr -> arr.toList() as List<Pair<MyFriendModel,SummonerModel>> }
        .map { lst -> lst.map { pair -> MyFriendData(pair.first, pair.second, pair.second.icon.blockingGet(), pair.second.name) } } // Observable.zip(lst.map { (friend, sum) -> sum.icon.map { bmp -> MyFriendData(friend, sum, bmp, sum.name) } }) { arr -> arr.toList() as List<MyFriendData> }

    //  LiveData Output  //////////////////////////////////////////////////////

    val currentRegionsLive: LiveData<List<Region>> = currentRegionsObservable.toLiveData()
    val mySummonersInSelectedRegionLive: LiveData<List<Triple<Drawable, SummonerModel,Boolean>>> = mySummonersInSelectedRegionObservable.toLiveData()
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
                    object : OneTimeObserver<List<Region>>(){
                        override fun onOneTimeChanged(lstRegs: List<Region>) {
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
        val icon: Drawable,
        val name: String,
    )
}
