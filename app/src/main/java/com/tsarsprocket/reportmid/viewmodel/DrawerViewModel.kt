package com.tsarsprocket.reportmid.viewmodel

import android.graphics.Bitmap
import android.view.View
import android.widget.LinearLayout
import androidx.lifecycle.*
import com.tsarsprocket.reportmid.model.RegionModel
import com.tsarsprocket.reportmid.model.Repository
import com.tsarsprocket.reportmid.model.SummonerModel
import com.tsarsprocket.reportmid.model.state.MyAccountModel
import com.tsarsprocket.reportmid.tools.OneTimeObserver
import com.tsarsprocket.reportmid.tools.toLiveData
import com.tsarsprocket.reportmid.tools.toObservable
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DrawerViewModel @Inject constructor(val repository: Repository) : ViewModel() {

    //  Rx  ///////////////////////////////////////////////////////////////////

    private val selectedRegionPositionObservable: Observable<Int>
    private val currentRegionsObservable: Observable<List<RegionModel>>
    private val mySummonersInSelectedRegionObservable: Observable<List<Triple<Bitmap,SummonerModel,Boolean>>>
    private val currentAccountObservable: Observable<List<MyAccountModel>>

    //  LiveData Representation  //////////////////////////////////////////////

    val currentRegionsLive: LiveData<List<RegionModel>>
    val selectedRegionPositionLive = MutableLiveData<Int>()  // In from spinner
    val mySummonersInSelectedRegionLive: LiveData<List<Triple<Bitmap,SummonerModel,Boolean>>>
    val currentAccountLive: LiveData<List<MyAccountModel>>

    //  Utility  //////////////////////////////////////////////////////////////

    private val disposer = CompositeDisposable()

    //  Init  /////////////////////////////////////////////////////////////////

    init {

        selectedRegionPositionObservable = selectedRegionPositionLive.toObservable()

        currentRegionsObservable = repository.getMyRegions().map { lst -> lst.sortedBy { it.title } }

        currentRegionsLive = currentRegionsObservable.toLiveData()

        mySummonersInSelectedRegionObservable =
            Observable.combineLatest( selectedRegionPositionObservable, currentRegionsObservable ) { pos, lst ->
                lst[pos]
            }
                .switchMap { reg ->
                    repository.getMySummonersObservableForRegion(reg)
                }
                .map { lst ->
                    lst.sortedWith { o1, o2 -> SUMO_COMP.compare(o1.first, o2.first) }
                        .map { (sum, isSelected) -> Triple(sum.icon.blockingFirst(), sum, isSelected) }
                }

        mySummonersInSelectedRegionLive = mySummonersInSelectedRegionObservable.toLiveData()

        currentAccountObservable = Observable.combineLatest( selectedRegionPositionObservable, currentRegionsObservable ) { pos, regs -> regs[pos] }
            .switchMap { repository.getCurrentAccountsPerRegionObservable(it) }

        currentAccountLive = currentAccountObservable.toLiveData()

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
}
