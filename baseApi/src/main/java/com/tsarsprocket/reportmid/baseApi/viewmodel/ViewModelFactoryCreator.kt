package com.tsarsprocket.reportmid.baseApi.viewmodel

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.savedstate.SavedStateRegistryOwner

fun interface ViewModelFactoryCreator {
    fun create(registryOwner: SavedStateRegistryOwner): AbstractSavedStateViewModelFactory
}