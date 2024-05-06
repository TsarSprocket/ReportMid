package com.tsarsprocket.reportmid.app.di

import com.tsarsprocket.reportmid.app_api.di.AppApi
import com.tsarsprocket.reportmid.base.di.PerApi
import com.tsarsprocket.reportmid.ksp_processor.annotation.LazyProxy
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
