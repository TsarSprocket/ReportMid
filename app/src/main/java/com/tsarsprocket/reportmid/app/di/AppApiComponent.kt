package com.tsarsprocket.reportmid.app.di

import com.tsarsprocket.reportmid.appApi.di.AppApi
import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.kspApi.annotation.LazyProxy
import dagger.Component

@LazyProxy
@PerApi
@Component(
    modules = [
        AppModule::class,
    ],
)
internal interface AppApiComponent : AppApi {

    @Component.Factory
    interface Factory {

        fun create(): AppApiComponent
    }
}
