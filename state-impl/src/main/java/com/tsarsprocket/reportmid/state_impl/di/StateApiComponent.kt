package com.tsarsprocket.reportmid.state_impl.di

import com.tsarsprocket.reportmid.app_api.di.AppApi
import com.tsarsprocket.reportmid.base.di.PerApi
import com.tsarsprocket.reportmid.state_api.di.StateApi
import dagger.Component

@PerApi
@Component(
    modules = [
        StateModule::class,
    ],
    dependencies = [
        AppApi::class,
    ]
)
internal interface StateApiComponent : StateApi {

    @Component.Factory
    interface Factory {
        fun create(appApi: AppApi): StateApiComponent
    }
}