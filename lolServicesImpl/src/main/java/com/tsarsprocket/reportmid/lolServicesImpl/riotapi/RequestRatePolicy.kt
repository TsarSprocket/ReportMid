package com.tsarsprocket.reportmid.lolServicesImpl.riotapi

import com.tsarsprocket.reportmid.baseApi.di.qualifiers.Computation
import com.tsarsprocket.reportmid.lolServicesApi.riotapi.RequestRateExceededException
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response
import kotlin.concurrent.atomics.AtomicInt
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.concurrent.atomics.decrementAndFetch
import kotlin.concurrent.atomics.incrementAndFetch
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalAtomicApi::class)
internal class RequestRatePolicy @AssistedInject constructor(
    @param:Assisted private val requestRate: Int,
    @param:Assisted private val intervalMillis: Long,
    @param:Computation private val computationDispatcher: CoroutineDispatcher,
): Interceptor {

    private val scope = CoroutineScope(SupervisorJob() + computationDispatcher.limitedParallelism(1))
    private val permits = AtomicInt(requestRate)

    override fun intercept(chain: Interceptor.Chain): Response {
        return if (permits.decrementAndFetch() < 0) {
            permits.incrementAndFetch()
            throw RequestRateExceededException(requestRate, intervalMillis.milliseconds)
        } else {
            scope.launch {
                @Suppress("ConvertLongToDuration")
                delay(intervalMillis)
                permits.incrementAndFetch()
            }
            chain.proceed(chain.request())
        }
    }
}
