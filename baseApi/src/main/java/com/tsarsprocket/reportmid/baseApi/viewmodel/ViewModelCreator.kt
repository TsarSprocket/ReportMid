package com.tsarsprocket.reportmid.baseApi.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

fun interface
ViewModelCreator {
    fun create(handle: SavedStateHandle): ViewModel
}