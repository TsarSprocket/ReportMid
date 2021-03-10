package com.tsarsprocket.reportmid.tools

import androidx.lifecycle.*
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable

fun <T> Observable<T>.toLiveData(): LiveData<T> =
    LiveDataReactiveStreams.fromPublisher(this.toFlowable(BackpressureStrategy.BUFFER))

fun <T> LiveData<T>.toObservable(): Observable<T> = Observable.create { emitter -> this.observeForever { item -> item?.let { emitter.onNext(it) } } }

fun <T> LiveData<T>.toFlowable(mode: BackpressureStrategy = BackpressureStrategy.LATEST): Flowable<T> =
    Flowable.create({ emitter -> this.observeForever { item -> item?.let { emitter.onNext(it) } } }, mode)
