package com.tsarsprocket.reportmid.tools

import androidx.lifecycle.LiveData
import androidx.lifecycle.toLiveData
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable

fun <T> Observable<T>.toLiveData(): LiveData<T> = this.toFlowable(BackpressureStrategy.BUFFER).toLiveData()

fun <T> LiveData<T>.toObservable(): Observable<T> = Observable.create { emitter -> this.observeForever { item -> item?.let { emitter.onNext(it) } } }

fun <T> LiveData<T>.toFlowable(mode: BackpressureStrategy = BackpressureStrategy.LATEST): Flowable<T> =
    Flowable.create({ emitter -> this.observeForever { item -> item?.let { emitter.onNext(it) } } }, mode)
