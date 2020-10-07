package com.tsarsprocket.reportmid.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tsarsprocket.reportmid.model.Repository
import com.tsarsprocket.reportmid.model.SummonerModel
import com.tsarsprocket.reportmid.model.state.MyAccountModel
import io.reactivex.BackpressureStrategy
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ManageFriendsViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    val myAccsAndSumms: LiveData<List<Triple<MyAccountModel,SummonerModel,Bitmap>>> = LiveDataReactiveStreams.fromPublisher(getMyAccsAndSumsObservable().toFlowable(BackpressureStrategy.BUFFER))

    val selectedAccPosition = MutableLiveData<Int>()

    val disposer = CompositeDisposable()

    fun init(myAccInitPos: Int) {
        if (selectedAccPosition.value == null) selectedAccPosition.value = myAccInitPos
    }

    private fun getMyAccsAndSumsObservable() = repository.getMyAccounts()
        .map { lst ->
            lst.mapNotNull { myAcc ->
                ( try { myAcc.summoner.blockingGet().let{ Pair(it,it.icon.blockingFirst()) } } catch (ex: RuntimeException) { null } )
                    ?.let { (sum, bmp) -> Triple(myAcc,sum,bmp) }
            }
        }
}
