package com.tsarsprocket.reportmid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.tsarsprocket.reportmid.model.Repository
import com.tsarsprocket.reportmid.summonerApi.model.Summoner
import com.tsarsprocket.reportmid.tools.toLiveData
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

const val TOP_MASTERIES_NUM = 5

class AddSummonerViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    val allRegions = Repository.allRegions
    val regionTitles
        get() = allRegions.map { it.title }

    val selectedRegionPosition = MutableLiveData<Int>()

    val activeSummonerName = MutableLiveData("")

    val activeSummoner = MutableLiveData<Summoner>()

    val selectedRegionName = selectedRegionPosition.map { pos -> if (pos >= 0) allRegions[pos].title else "<not selected>" }

    //  Methods  //////////////////////////////////////////////////////////////

    fun checkSummoner() = repository.findSummonerForName(
        activeSummonerName.value ?: "",
        allRegions[selectedRegionPosition.value!!], failOnNotFound = true
    )
        .observeOn(AndroidSchedulers.mainThread())
        .map { summonerModel ->
            Maybe.just(summonerModel)
        }.onErrorReturn {
            Maybe.empty()
        }
        .toLiveData()
        .map {
            if(!it.isEmpty.blockingGet()) activeSummoner.value = it.blockingGet()
            it
        }

    fun isSummonerInUseLive(sum: Summoner): LiveData<Boolean> = repository.checkSummonerInUse(sum).toLiveData()
}
