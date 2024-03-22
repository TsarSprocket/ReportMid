package com.tsarsprocket.reportmid.viewmodel

import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tsarsprocket.reportmid.RIOTIconProvider
import com.tsarsprocket.reportmid.lol.model.Region
import com.tsarsprocket.reportmid.model.Repository
import com.tsarsprocket.reportmid.model.my_friend.MyFriendModel
import com.tsarsprocket.reportmid.state_api.data.StateRepository
import com.tsarsprocket.reportmid.summoner_api.data.SummonerRepository
import com.tsarsprocket.reportmid.summoner_api.model.MyAccount
import com.tsarsprocket.reportmid.summoner_api.model.Summoner
import com.tsarsprocket.reportmid.tools.OneTimeObserver
import com.tsarsprocket.reportmid.tools.Optional
import com.tsarsprocket.reportmid.tools.toLiveData
import com.tsarsprocket.reportmid.tools.toObservable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.rx2.rxSingle
import javax.inject.Inject

class DrawerViewModel @Inject constructor(
    val repository: Repository,
    private val summonerRepository: SummonerRepository,
    private val stateRepository: StateRepository,
    private val iconProvider: RIOTIconProvider,
) : ViewModel() {

    //  LiveData Input  ///////////////////////////////////////////////////////

    val selectedRegionPositionLive = MutableLiveData<Int>()  // In from spinner

    //  Rx  ///////////////////////////////////////////////////////////////////

    private val selectedRegionPositionObservable: Observable<Int> = selectedRegionPositionLive.toObservable()

    private val currentRegionsObservable: Observable<List<Region>> = repository.getMyRegions().map { lst -> lst.sortedBy { it.title } }

    private val mySummonersInSelectedRegionObservable: Observable<List<Triple<Drawable, Summoner, Boolean>>> =
        Observable.combineLatest( selectedRegionPositionObservable, currentRegionsObservable ) { pos, lst -> lst[pos] }
            .switchMapSingle { reg -> rxSingle { getMySummonersForRegion(reg) } }
            .map { lst ->
                lst.sortedWith { o1, o2 -> SUMO_COMP.compare(o1.first, o2.first) }
                    .map { (sum, isSelected) -> Triple(iconProvider.getProfileIcon(sum.iconId).blockingGet(), sum, isSelected) }
            }

    private val myCurrentAccountsObservable: Observable<Optional<MyAccount>> =
        Observable.combineLatest( selectedRegionPositionObservable, currentRegionsObservable ) { pos, regs -> regs[pos] }
            .switchMap { repository.getMyCurrentAccountsPerRegionObservable(it) }

    private val myFriendsRx: Observable<List<MyFriendData>> = myCurrentAccountsObservable
        .filter { optional -> optional.hasValue }
        .switchMap { repository.getFriendsForAcc(it.value) }
        .map { lst -> lst.map { fr -> fr to fr.summoner.blockingGet() } } // Observable.zip(lst.map { friend -> friend.summoner.toObservable().map { sum -> friend to sum } }) { arr -> arr.toList() as List<Pair<MyFriendModel,SummonerModel>> }
        .map { lst ->
            lst.map { pair ->
                MyFriendData(
                    pair.first,
                    pair.second,
                    iconProvider.getProfileIcon(pair.second.iconId).blockingGet(),
                    pair.second.name
                )
            }
        } // Observable.zip(lst.map { (friend, sum) -> sum.icon.map { bmp -> MyFriendData(friend, sum, bmp, sum.name) } }) { arr -> arr.toList() as List<MyFriendData> }

    //  LiveData Output  //////////////////////////////////////////////////////

    val currentRegionsLive: LiveData<List<Region>> = currentRegionsObservable.toLiveData()
    val mySummonersInSelectedRegionLive: LiveData<List<Triple<Drawable, Summoner, Boolean>>> = mySummonersInSelectedRegionObservable.toLiveData()
    val currentAccountLive: LiveData<Optional<MyAccount>> = myCurrentAccountsObservable.toLiveData()
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

    fun activateAcc(sum: Summoner) {
        disposer.add(repository.getMyAccountForSummoner(sum.puuid, sum.region)
            .observeOn(Schedulers.io())
            .subscribe { myAccount ->
                repository.activateAccount(myAccount)
            })
    }

    override fun onCleared() {
        disposer.clear()
    }

    /**
     * Fetches the list of summoners for
     * @param region - the region for which to select the summoners
     * @return list of pairs (summoner, selection flag)
     */
    private suspend fun getMySummonersForRegion(region: Region): List<Pair<Summoner, Boolean>> {
        val currentAccount = stateRepository.getCurrentAccountByRegion(region)
        val myAccount = summonerRepository.getMyAccountById(currentAccount.myAccountId)
        return summonerRepository.getMySummonersForRegion(region).map { summoner -> Pair(summoner, summoner.id == myAccount.summonerId) }
    }

    //  Static  ///////////////////////////////////////////////////////////////

    companion object {
        val SUMO_COMP = Summoner.ByNameAndRegionComparator()
    }

    //  Classes  //////////////////////////////////////////////////////////////

    data class MyFriendData(
        val friend: MyFriendModel,
        val sum: Summoner,
        val icon: Drawable,
        val name: String,
    )
}
