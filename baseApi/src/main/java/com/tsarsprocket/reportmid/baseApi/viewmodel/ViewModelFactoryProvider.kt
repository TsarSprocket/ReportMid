package com.tsarsprocket.reportmid.baseApi.viewmodel

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner

interface ViewModelFactoryProvider {
    fun provide(viewModelClass: Class<out ViewModel>, registryOwner: SavedStateRegistryOwner): AbstractSavedStateViewModelFactory
}