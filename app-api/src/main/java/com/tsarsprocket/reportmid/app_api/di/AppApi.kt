package com.tsarsprocket.reportmid.app_api.di

import android.content.Context
import androidx.fragment.app.Fragment
import com.tsarsprocket.reportmid.app_api.room.MainStorage
import com.tsarsprocket.reportmid.base_api.di.qualifiers.Aggregated
import com.tsarsprocket.reportmid.base_api.di.qualifiers.Computation
import com.tsarsprocket.reportmid.base_api.di.qualifiers.Io
import com.tsarsprocket.reportmid.base_api.di.qualifiers.Ui
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