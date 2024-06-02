package com.tsarsprocket.reportmid.appApi.di

import android.content.Context
import androidx.fragment.app.Fragment
import com.tsarsprocket.reportmid.appApi.room.MainStorage
import com.tsarsprocket.reportmid.baseApi.di.qualifiers.Aggregated
import com.tsarsprocket.reportmid.baseApi.di.qualifiers.Computation
import com.tsarsprocket.reportmid.baseApi.di.qualifiers.Io
import com.tsarsprocket.reportmid.baseApi.di.qualifiers.Ui
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Provider

interface AppApi {
    @AppContext
    fun getAppContext(): Context

    @Ui
    fun getUiDespatcher(): CoroutineDispatcher

    @Io
    fun getIoDispatcher(): CoroutineDispatcher

    @Computation
    fun getComputationDispatcher(): CoroutineDispatcher

    fun getMainStorage(): MainStorage

    @Aggregated
    fun getFragmentCreators(): Map<Class<out Fragment>, Provider<Fragment>>
}