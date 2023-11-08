package com.tsarsprocket.reportmid.view_state_api.view_state

interface ViewStateController {
    fun postIntent(intent: ViewIntent, goBackState: ViewState? = null)
    fun postEffect(effect: ViewEffect)
}