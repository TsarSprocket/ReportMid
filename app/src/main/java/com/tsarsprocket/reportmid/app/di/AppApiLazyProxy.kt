package com.tsarsprocket.reportmid.app.di

import android.content.Context
import com.tsarsprocket.reportmid.app_api.di.AppApi
import com.tsarsprocket.reportmid.app_api.request_manager.RequestManager
import com.tsarsprocket.reportmid.app_api.room.MainStorage
import kotlinx.coroutines.CoroutineDispatcher

class AppApiLazyProxy(appApiProducer: () -> AppApi) : AppApi {

    private val appApi by lazy(appApiProducer)

    override fun getAppContext(): Context = appApi.getAppContext()

    override fun getUiDespatcher(): CoroutineDispatcher = appApi.getUiDespatcher()

    override fun getIoDispatcher(): CoroutineDispatcher = appApi.getIoDispatcher()

    override fun getComputationDispatcher(): CoroutineDispatcher = appApi.getComputationDispatcher()

    override fun getMainStorage(): MainStorage = appApi.getMainStorage()

    override fun getRequestManager(): RequestManager = appApi.getRequestManager()
}