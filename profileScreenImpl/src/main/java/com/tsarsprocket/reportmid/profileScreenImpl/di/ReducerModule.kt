package com.tsarsprocket.reportmid.profileScreenImpl.di

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.profileScreenApi.viewIntent.ShowProfileScreenViewIntent
import com.tsarsprocket.reportmid.profileScreenImpl.reducer.ProfileScreenReducer
import com.tsarsprocket.reportmid.viewStateApi.di.ViewIntentKey
import com.tsarsprocket.reportmid.viewStateApi.reducer.Reducer
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal interface ReducerModule {

    @Binds
    @PerApi
    @IntoMap
    @ViewIntentKey(ShowProfileScreenViewIntent::class)
    fun bindToShowProfileScreenViewIntent(reducer: ProfileScreenReducer): Reducer
}