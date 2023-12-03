package com.tsarsprocket.reportmid.app.di

import android.content.Context
import com.tsarsprocket.reportmid.app_api.di.AppApi
import kotlinx.coroutines.CoroutineDispatcher

class AppApiLazyProxy(appApiProducer: () -> AppApi) : AppApi {

    private val appApi by lazy(appApiProducer)

    override fun getAppContext(): Context = appApi.getAppContext()

    override fun getUiDespatcher(): CoroutineDispatcher = appApi.getUiDespatcher()

    override fun getIoDispatcher(): CoroutineDispatcher = appApi.getIoDispatcher()

    override fun getComputationDispatcher(): CoroutineDispatcher = appApi.getComputationDispatcher()
}