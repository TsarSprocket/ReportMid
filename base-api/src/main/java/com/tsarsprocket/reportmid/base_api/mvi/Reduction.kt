package com.tsarsprocket.reportmid.base_api.mvi

data class Reduction<State, ViewState>(val state: State, val viewState: ViewState?)
