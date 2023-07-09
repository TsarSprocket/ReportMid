package com.tsarsprocket.reportmid.lol_services_impl.riotapi

import io.reactivex.subjects.PublishSubject
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit

class RequestRatePolicy(
    private val requestRate: Int,
    private val interval: Long,
    private val timeUnit: TimeUnit,
): Interceptor {

    val semaphore = Semaphore(requestRate)

    val releaser = PublishSubject.create<Unit>().also {
        it.throttleLast(interval, timeUnit).subscribe{ resetPermissions() }
    }

    @Synchronized
    override fun intercept(chain: Interceptor.Chain): Response {
        semaphore.acquire()
        releaser.onNext(Unit)
        return chain.proceed(chain.request())
    }

    @Synchronized
    private fun resetPermissions() {
        val availablePermits = semaphore.availablePermits()
        if (availablePermits < requestRate) {
            semaphore.release(requestRate - availablePermits)
            releaser.onNext(Unit)
        }
    }
}
