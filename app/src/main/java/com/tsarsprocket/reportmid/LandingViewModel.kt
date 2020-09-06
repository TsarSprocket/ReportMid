package com.tsarsprocket.reportmid

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tsarsprocket.reportmid.model.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class LandingViewModel @Inject constructor( val repository: Repository ) : ViewModel() {

    enum class STATE { LOADING, FOUND, NOT_FOUND }

    var puuid = ""
    val stateLive = MutableLiveData( STATE.LOADING )

    val disposer = CompositeDisposable()


    init {
        disposer.add( repository.getActiveSummonerPUUID()
            .observeOn( AndroidSchedulers.mainThread() )
            .doOnNext{ puuid ->
                this.puuid = puuid
                stateLive.value = STATE.FOUND
            }
            .doOnComplete { if( stateLive.value != STATE.FOUND ) stateLive.value = STATE.NOT_FOUND }
            .subscribe() )
    }

    override fun onCleared() {
        disposer.clear()
        super.onCleared()
    }
}