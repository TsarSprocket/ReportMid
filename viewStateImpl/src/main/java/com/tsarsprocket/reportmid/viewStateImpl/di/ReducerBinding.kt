package com.tsarsprocket.reportmid.viewStateImpl.di

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.viewStateApi.di.ViewIntentKey
import com.tsarsprocket.reportmid.viewStateApi.reducer.Reducer
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.QuitViewIntent
import com.tsarsprocket.reportmid.viewStateImpl.reducer.DefaultReducer
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ReducerBinding {

    @Binds
    @PerApi
    @IntoMap
    @ViewIntentKey(QuitViewIntent::class)
    fun bindToQuitViewIntent(reducer: DefaultReducer): Reducer
}