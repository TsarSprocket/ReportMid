package com.tsarsprocket.reportmid.tools

import androidx.lifecycle.*
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable

fun <T> Observable<T>.toLiveData(): LiveData<T> =
    LiveDataReactiveStreams.fromPublisher(this.toFlowable(BackpressureStrategy.BUFFER))

fun <T> LiveData<T>.toObservable(): Observable<T> = Observable.create { emitter -> this.observeForever { item -> item?.let { emitter.onNext(it) } } }
