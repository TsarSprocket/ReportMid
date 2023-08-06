package com.tsarsprocket.reportmid.app_api.di

import android.content.Context
import com.tsarsprocket.reportmid.base.di.Api

interface AppApi : Api {
    fun getAppContext(): Context
}