package com.tsarsprocket.reportmid.app.di

import com.tsarsprocket.reportmid.app_api.capability.AppApi
import dagger.Component

@Component(
    modules = [
        AppModule::class,
    ]
)
interface AppComponent : AppApi
