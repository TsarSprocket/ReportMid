package com.tsarsprocket.reportmid.overview.viewmodel

import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tsarsprocket.reportmid.model.PuuidAndRegion
import com.tsarsprocket.reportmid.summoner.model.SummonerModel
import com.tsarsprocket.reportmid.summoner.model.SummonerRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx2.asFlow
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ProfileOverviewViewModel @Inject constructor(
    private val summonerRepository: SummonerRepository,
): ViewModel() {

    val puuidAndRegion = MutableSharedFlow<PuuidAndRegion>()
    val summoner: SharedFlow<SummonerModel> = getSummonerFlow( puuidAndRegion ).shareIn( viewModelScope, SharingStarted.WhileSubscribed(), replay = 1 )
    val summonerLevel = MutableLiveData<String>()
    val summonerName = MutableLiveData<String>()
    val summonerIconLive = MutableLiveData<Drawable>()

    init {
        viewModelScope.launch {
            summoner.flatMapLatest { it.icon.toObservable().asFlow() }.collect { summonerIconLive.value = it }
        }
        viewModelScope.launch {
            summoner.collect {
                summonerName.value = it.name
                summonerLevel.value = it.level.toString()
            }
        }
    }

    fun getMasteriesFlow(): Flow<List<MasteryViewModel>> {
        return summoner.flatMapLatest { summonerModel -> summonerModel.masteries.toObservable().asFlow() }
            .map { masteriesList ->
                masteriesList.map model@{ masteryModel ->
                    val iconFlow = masteryModel.champion.switchMapSingle { championModel -> championModel.icon }
                        .asFlow()
                        .shareIn( viewModelScope, SharingStarted.WhileSubscribed(), replay = 1 )

                    return@model MasteryViewModel( iconFlow, masteryModel.level, masteryModel.points )
                }
            }
    }

    // Implementation //////////////////////////////////////////////////////////

    @ExperimentalCoroutinesApi
    private fun getSummonerFlow( puuidAndRegionFlow: Flow<PuuidAndRegion> ): Flow<SummonerModel> = puuidAndRegionFlow
        .distinctUntilChanged()
        .flatMapLatest { puuidAndRegion -> summonerRepository.getByPuuidAndRegion( puuidAndRegion ).toObservable().asFlow() }

    data class MasteryViewModel(
        val icon: Flow<Drawable>,
        val level: Int,
        val points: Int,
    )
}