package com.tsarsprocket.reportmid.view_state_impl.di

import com.tsarsprocket.reportmid.app_api.di.AppApi
import com.tsarsprocket.reportmid.base.di.FragmentsCreator
import com.tsarsprocket.reportmid.base.di.PerApi
import com.tsarsprocket.reportmid.view_state_api.di.EffectHandlersProvider
import com.tsarsprocket.reportmid.view_state_api.di.StateReducersProvider
import com.tsarsprocket.reportmid.view_state_api.di.StateVisualizersProvider
import com.tsarsprocket.reportmid.view_state_api.di.ViewStateApi
import dagger.Component

@PerApi
@Component(
    modules = [
        ViewStateModule::class,
    ],
    dependencies = [
        AppApi::class,
    ]
)
internal interface ViewStateApiComponent : ViewStateApi, FragmentsCreator, StateVisualizersProvider, EffectHandlersProvider, StateReducersProvider {

    @Component.Factory
    interface Factory {

        fun create(appApi: AppApi): ViewStateApiComponent
    }
}