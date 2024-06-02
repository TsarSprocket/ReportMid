package com.tsarsprocket.reportmid.overview.viewmodel

import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tsarsprocket.reportmid.RIOTIconProvider
import com.tsarsprocket.reportmid.dataDragonApi.data.DataDragon
import com.tsarsprocket.reportmid.lol.model.PuuidAndRegion
import com.tsarsprocket.reportmid.summonerApi.model.Summoner
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx2.asFlow
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ProfileOverviewViewModel @Inject constructor(
    private val summonerRepository: com.tsarsprocket.reportmid.summonerApi.data.SummonerRepository,
    private val dataDragon: DataDragon,
    private val iconProvider: RIOTIconProvider,
) : ViewModel() {

    val puuidAndRegion = MutableSharedFlow<PuuidAndRegion>()
    val summonerFlow: SharedFlow<Summoner> = getSummonerFlow(puuidAndRegion).shareIn(viewModelScope, SharingStarted.WhileSubscribed(), replay = 1)
    val summonerLevel = MutableLiveData<String>()
    val summonerName = MutableLiveData<String>()
    val summonerIconLive = MutableLiveData<Drawable>()

    init {
        viewModelScope.launch {
            summonerFlow.flatMapLatest { iconProvider.getProfileIcon(it.iconId).toObservable().asFlow() }.collect { summonerIconLive.value = it }
        }
        viewModelScope.launch {
            summonerFlow.collect {
                summonerName.value = it.name
                summonerLevel.value = it.level.toString()
            }
        }
    }

    fun getMasteriesFlow(): Flow<List<MasteryViewModel>> {
        return summonerFlow
            .map { summoner ->
                summoner.masteriesProvider
                    .get()
                    .map { masteryModel ->
                        val iconFlow = iconProvider.getChampionIcon(dataDragon.tail.getChampionById(masteryModel.championId).iconName)
                            .toObservable()
                        .asFlow()
                        .shareIn(viewModelScope, SharingStarted.WhileSubscribed(), replay = 1)

                        MasteryViewModel(iconFlow, masteryModel.level, masteryModel.points)
                }
            }
    }

    // Implementation //////////////////////////////////////////////////////////

    @ExperimentalCoroutinesApi
    private fun getSummonerFlow(puuidAndRegionFlow: Flow<PuuidAndRegion>): Flow<Summoner> = puuidAndRegionFlow
        .distinctUntilChanged()
        .map { puuidAndRegion -> summonerRepository.requestRemoteSummonerByPuuidAndRegion(puuidAndRegion) }

    data class MasteryViewModel(
        val icon: Flow<Drawable>,
        val level: Int,
        val points: Int,
    )
}