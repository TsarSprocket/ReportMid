package com.tsarsprocket.reportmid.base.mvi

data class Reduction<State, ViewState>(val state: State, val viewState: ViewState?)
