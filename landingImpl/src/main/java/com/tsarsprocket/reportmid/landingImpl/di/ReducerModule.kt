package com.tsarsprocket.reportmid.landingImpl.di

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.landingApi.viewIntent.LandingIntent.LandingStartLoadViewIntent
import com.tsarsprocket.reportmid.landingApi.viewIntent.LandingIntent.SummonerFoundViewIntent
import com.tsarsprocket.reportmid.landingImpl.reducer.LandingReducer
import com.tsarsprocket.reportmid.landingImpl.viewIntent.InternalLandingIntent.DataDragonNotLoadedViewIntent
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
    @ViewIntentKey(DataDragonNotLoadedViewIntent::class)
    fun bindToDataDragonNotLoadedViewIntent(reducer: LandingReducer): Reducer

    @Binds
    @PerApi
    @IntoMap
    @ViewIntentKey(LandingStartLoadViewIntent::class)
    fun bindToLandingStartLoadViewIntent(reducer: LandingReducer): Reducer

    @Binds
    @PerApi
    @IntoMap
    @ViewIntentKey(SummonerFoundViewIntent::class)
    fun bindToSummonerFoundViewIntent(reducer: LandingReducer): Reducer
}