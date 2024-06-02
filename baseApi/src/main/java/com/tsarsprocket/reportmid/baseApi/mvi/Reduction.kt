package com.tsarsprocket.reportmid.baseApi.mvi

data class Reduction<State, ViewState>(val state: State, val viewState: ViewState?)
