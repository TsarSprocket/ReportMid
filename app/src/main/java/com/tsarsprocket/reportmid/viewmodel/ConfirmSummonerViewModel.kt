package com.tsarsprocket.reportmid.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import com.tsarsprocket.reportmid.RIOTIconProvider
import com.tsarsprocket.reportmid.lol.model.PuuidAndRegion
import com.tsarsprocket.reportmid.model.Repository
import com.tsarsprocket.reportmid.summoner.model.SummonerRepository
import com.tsarsprocket.reportmid.tools.toLiveData
import io.reactivex.subjects.ReplaySubject
import javax.inject.Inject

class ConfirmSummonerViewModel @Inject constructor(
    val repository: Repository,
    private val summonerRepository: SummonerRepository,
    private val iconProvider: RIOTIconProvider,
): ViewModel() {

    val puuidSubj = ReplaySubject.create<PuuidAndRegion>( 1 )
    val summoner = puuidSubj.switchMapSingle { puuidAndRegion -> summonerRepository.getByPuuidAndRegion(puuidAndRegion) }.toLiveData()
    val bitmap = summoner.switchMap { sum -> iconProvider.getProfileIcon(sum.iconName).toObservable().toLiveData() }
    val name = summoner.map { sum -> sum.name }
    val level = summoner.map { sum -> sum.level.toString() }
    val confirm = MutableLiveData<Boolean>()

    fun init( puuidAndRegion: PuuidAndRegion) {
        if( puuidSubj.value != puuidAndRegion ) puuidSubj.onNext( puuidAndRegion )
    }

    fun confirm( view: View ) {
        confirm.value = true
    }

    fun decline( view: View ) {
        confirm.value = false
    }
}