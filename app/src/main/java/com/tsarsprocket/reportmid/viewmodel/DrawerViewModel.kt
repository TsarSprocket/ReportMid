package com.tsarsprocket.reportmid.viewmodel

import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import com.tsarsprocket.reportmid.model.Repository
import io.reactivex.BackpressureStrategy
import javax.inject.Inject

class DrawerViewModel @Inject constructor( val repository: Repository ) : ViewModel() {

    val currentRegions = repository.getCurrentRegions()
    val regionTitles = LiveDataReactiveStreams.fromPublisher( currentRegions.map { list -> list.map { reg -> reg.title } }.toFlowable( BackpressureStrategy.LATEST ) )

}