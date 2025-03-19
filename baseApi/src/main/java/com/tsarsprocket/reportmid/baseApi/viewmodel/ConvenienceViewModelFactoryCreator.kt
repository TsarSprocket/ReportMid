package com.tsarsprocket.reportmid.baseApi.viewmodel

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner

class ConvenienceViewModelFactoryCreator(
    private val defaultArgs: Bundle?,
    private val viewModelCreator: ViewModelCreator,
) : ViewModelFactoryCreator {

    override fun create(registryOwner: SavedStateRegistryOwner) = object : AbstractSavedStateViewModelFactory(registryOwner, defaultArgs) {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(key: String, modelClass: Class<T>, handle: SavedStateHandle) = viewModelCreator.create(handle) as T
    }
}