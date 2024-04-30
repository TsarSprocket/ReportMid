package com.tsarsprocket.reportmid.app_api.di

import android.content.Context
import com.tsarsprocket.reportmid.app_api.request_manager.RequestManager
import com.tsarsprocket.reportmid.app_api.room.MainStorage
import com.tsarsprocket.reportmid.base.di.qualifiers.Computation
import com.tsarsprocket.reportmid.base.di.qualifiers.Io
import com.tsarsprocket.reportmid.base.di.qualifiers.Ui
import com.tsarsprocket.reportmid.lazy_proxy_ksp.annotation.LazyProxy
import kotlinx.coroutines.CoroutineDispatcher

@LazyProxy
interface AppApi {
    fun getAppContext(): Context

    @Ui
    fun getUiDespatcher(): CoroutineDispatcher

    @Io
    fun getIoDispatcher(): CoroutineDispatcher

    @Computation
    fun getComputationDispatcher(): CoroutineDispatcher

    fun getMainStorage(): MainStorage

    fun getRequestManager(): RequestManager
}