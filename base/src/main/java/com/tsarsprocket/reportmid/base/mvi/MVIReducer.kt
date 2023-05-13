package com.tsarsprocket.reportmid.base.mvi

interface MVIReducer<State, Intent, ViewState, ViewEffect>: (State, Intent, MVIProcessor<State, Intent, ViewState, ViewEffect>) -> State