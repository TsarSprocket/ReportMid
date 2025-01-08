package com.tsarsprocket.reportmid.viewStateApi.viewIntent

import android.os.Parcelable
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewStateHolder

interface ViewIntent : Parcelable {
    suspend fun reduce(state: ViewState, stateHolder: ViewStateHolder): ViewState
}
