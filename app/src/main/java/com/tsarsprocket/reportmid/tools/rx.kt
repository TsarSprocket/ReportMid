package com.tsarsprocket.reportmid.tools

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.ReplaySubject

fun <T> Observable<T>.toReplaySubject(n: Int): ReplaySubject<T> = ReplaySubject.create<T>(n).also { this.subscribe(it) }

fun <T> Observable<T>.toPublishSubject(): PublishSubject<T> = PublishSubject.create<T>().also { this.subscribe(it) }