package com.tsarsprocket.reportmid.app_impl.di

import com.tsarsprocket.reportmid.app_api.di.AppApi
import com.tsarsprocket.reportmid.base.di.AppScope
import dagger.Component

@AppScope
@Component(
    modules = [
        AppModule::class,
        AppContextProviderModule::class,
    ]
)
internal interface AppApiComponent : AppApi {

    @Component.Factory
    interface Factory {
        fun create(appContextProviderModule: AppContextProviderModule): AppApiComponent
    }
}