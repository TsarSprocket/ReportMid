package com.tsarsprocket.reportmid.findSummonerImpl.di

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.findSummonerApi.viewIntent.FindSummonerViewIntent
import com.tsarsprocket.reportmid.findSummonerImpl.reducer.FindSummonerReducer
import com.tsarsprocket.reportmid.findSummonerImpl.viewIntent.FindAndConfirmSummonerViewIntent
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
    @ViewIntentKey(FindAndConfirmSummonerViewIntent::class)
    fun bindReducerToFindAndConfirmSummonerViewIntent(reducer: FindSummonerReducer): Reducer

    @Binds
    @PerApi
    @IntoMap
    @ViewIntentKey(FindSummonerViewIntent::class)
    fun bindReducerToFindSummonerViewStateReducer(reducer: FindSummonerReducer): Reducer
}