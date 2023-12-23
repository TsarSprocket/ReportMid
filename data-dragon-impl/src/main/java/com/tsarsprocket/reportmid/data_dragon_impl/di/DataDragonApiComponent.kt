package com.tsarsprocket.reportmid.data_dragon_impl.di

import com.tsarsprocket.reportmid.base.di.PerApi
import com.tsarsprocket.reportmid.data_dragon_api.di.DataDragonApi
import dagger.Component

@PerApi
@Component(
    modules = [
        DataDragonModule::class,
    ]
)
interface DataDragonApiComponent : DataDragonApi {

    @Component.Factory
    interface Factory {
        fun create(): DataDragonApiComponent
    }
}