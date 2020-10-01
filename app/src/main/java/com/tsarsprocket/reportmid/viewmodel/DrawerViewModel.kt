package com.tsarsprocket.reportmid.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.*
import com.tsarsprocket.reportmid.model.RegionModel
import com.tsarsprocket.reportmid.model.Repository
import com.tsarsprocket.reportmid.model.SummonerModel
import com.tsarsprocket.reportmid.tools.OneTimeObserver
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class DrawerViewModel @Inject constructor(val repository: Repository) : ViewModel() {

    val currentRegions: LiveData<List<RegionModel>> = obtainCurrentRegionsLive()
    val regionTitles: LiveData<List<String>> = currentRegions.map { list -> list.map { reg -> reg.title } }
    val selectedRegionPosition = MutableLiveData<Int>()
    val mySummonersInSelectedRegion = selectedRegionPosition.map { currentRegions.value!![it] }.switchMap { reg ->
        LiveDataReactiveStreams.fromPublisher<List<Triple<Bitmap,String,Boolean>>>(
            repository.getMySummonersObservableForRegion(reg)
                .map { lst ->
                    lst.sortedWith { o1, o2 -> SUMO_COMP.compare(o1.first, o2.first) }
                        .map { (sum, isSelected) -> Triple(sum.icon.blockingFirst(), sum.name, isSelected) }
                }
                .toFlowable(BackpressureStrategy.BUFFER)
        )
    }

    val disposer = CompositeDisposable()

    init {
        disposer.add(
            repository.getSelectedAccountRegion()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { selReg ->
                    object : OneTimeObserver<List<RegionModel>>(){
                        override fun onOneTimeChanged(lstRegs: List<RegionModel>) {
                            selectedRegionPosition.value = lstRegs.indexOf(selReg)
                        }
                    }.observeForeverOn(currentRegions)
                }
        )
    }

    private fun obtainCurrentRegionsLive(): LiveData<List<RegionModel>> {
        return LiveDataReactiveStreams.fromPublisher(repository.getMyRegions().map { lstNewRegions ->
            val curRegs = getCurrentRegionsValue()
            val curSelPos = selectedRegionPosition.value
            if (curRegs != null && curSelPos != null) {
                val oldRegionTag = curRegs[curSelPos]
                val newPos = lstNewRegions.indexOf(oldRegionTag)
                if (newPos >= 0) selectedRegionPosition.postValue(newPos)
            }
            lstNewRegions
        }.toFlowable(BackpressureStrategy.BUFFER))
    }

    private fun getCurrentRegionsValue() = currentRegions.value

    override fun onCleared() {
        disposer.clear()
    }

    companion object {
        val SUMO_COMP = SummonerModel.ByNameAndRegionComparator()
    }
}
