package com.tsarsprocket.reportmid.stateImpl.di

import com.tsarsprocket.reportmid.appApi.di.AppApi
import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.stateApi.di.StateApi
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