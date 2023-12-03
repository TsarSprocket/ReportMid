package com.tsarsprocket.reportmid.app_api.di

import android.content.Context
import com.tsarsprocket.reportmid.base.di.qualifiers.Computation
import com.tsarsprocket.reportmid.base.di.qualifiers.Io
import com.tsarsprocket.reportmid.base.di.qualifiers.Ui
import kotlinx.coroutines.CoroutineDispatcher

interface AppApi {
    fun getAppContext(): Context

    @Ui
    fun getUiDespatcher(): CoroutineDispatcher

    @Io
    fun getIoDispatcher(): CoroutineDispatcher
    @Computation
    fun getComputationDispatcher(): CoroutineDispatcher
}