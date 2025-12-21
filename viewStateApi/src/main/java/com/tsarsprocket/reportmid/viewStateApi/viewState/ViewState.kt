package com.tsarsprocket.reportmid.viewStateApi.viewState

import android.os.Parcelable
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ShowStateViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder

interface ViewState : Parcelable {
    fun getRestoreStateIntent(): ViewIntent = ShowStateViewIntent(this)
    // These methods should only be implemented by state that has a lifecycle (e.g. incorporates a state holder)
    fun setParentHolder(parentHolder: ViewStateHolder) {}
    fun start() {}
    fun stop() {}
}
