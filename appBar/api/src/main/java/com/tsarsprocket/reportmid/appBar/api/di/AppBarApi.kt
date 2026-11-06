package com.tsarsprocket.reportmid.appBar.api.di

import com.tsarsprocket.reportmid.appBar.api.navigation.AppBarInitialViewState
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState

interface AppBarApi {
    @AppBarInitialViewState
    fun getAppBarInitialViewState(): ViewState
}
