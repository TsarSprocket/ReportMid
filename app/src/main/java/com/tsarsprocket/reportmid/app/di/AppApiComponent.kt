package com.tsarsprocket.reportmid.app.di

import com.tsarsprocket.reportmid.app_api.di.AppApi
import dagger.Component

@Component(
    modules = [
        AppModule::class,
    ]
)
internal interface AppApiComponent : AppApi
