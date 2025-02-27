package com.tsarsprocket.reportmid.findSummonerImpl.di

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.findSummonerImpl.effectHandler.FindSummonerEffectHandler
import com.tsarsprocket.reportmid.findSummonerImpl.viewEffect.ShowSnackViewEffect
import com.tsarsprocket.reportmid.viewStateApi.di.ViewEffectKey
import com.tsarsprocket.reportmid.viewStateApi.effectHandler.ViewEffectHandler
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface EffectHandlerModule {

    @Binds
    @PerApi
    @IntoMap
    @ViewEffectKey(ShowSnackViewEffect::class)
    fun bindToShowSnackViewEffect(handler: FindSummonerEffectHandler): ViewEffectHandler
}