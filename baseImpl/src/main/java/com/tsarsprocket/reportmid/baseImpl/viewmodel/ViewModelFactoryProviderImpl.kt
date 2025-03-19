package com.tsarsprocket.reportmid.baseImpl.viewmodel

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.tsarsprocket.reportmid.baseApi.di.qualifiers.Aggregated
import com.tsarsprocket.reportmid.baseApi.viewmodel.ViewModelFactoryCreator
import com.tsarsprocket.reportmid.baseApi.viewmodel.ViewModelFactoryProvider
import javax.inject.Inject
import javax.inject.Provider

class ViewModelFactoryProviderImpl @Inject constructor(
    @Aggregated private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModelFactoryCreator>>,
) : ViewModelFactoryProvider {

    override fun provide(viewModelClass: Class<out ViewModel>, registryOwner: SavedStateRegistryOwner): AbstractSavedStateViewModelFactory {
        val creator = creators[viewModelClass] ?: creators.entries.find { (key, _) -> viewModelClass.isAssignableFrom(key) }?.value ?: throw IllegalArgumentException("Unknown model: $viewModelClass")
        return creator.get().create(registryOwner)
    }
}