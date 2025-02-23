package com.tsarsprocket.reportmid.viewStateImpl.di

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.viewStateApi.di.ViewEffectKey
import com.tsarsprocket.reportmid.viewStateApi.effectHandler.EffectHandler
import com.tsarsprocket.reportmid.viewStateApi.viewEffect.GoBackViewEffect
import com.tsarsprocket.reportmid.viewStateApi.viewEffect.QuitViewEffect
import com.tsarsprocket.reportmid.viewStateImpl.effectHandler.DefaultEffectHandler
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface EffectHandlerModule {

    @Binds
    @PerApi
    @IntoMap
    @ViewEffectKey(GoBackViewEffect::class)
    fun bindToGoBackViewEffect(effectHandler: DefaultEffectHandler): EffectHandler

    @Binds
    @PerApi
    @IntoMap
    @ViewEffectKey(QuitViewEffect::class)
    fun bindToQuitViewEffect(effectHandler: DefaultEffectHandler): EffectHandler
}