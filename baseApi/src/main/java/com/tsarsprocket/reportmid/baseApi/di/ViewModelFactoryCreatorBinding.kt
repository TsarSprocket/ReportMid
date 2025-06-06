package com.tsarsprocket.reportmid.baseApi.di

import androidx.lifecycle.ViewModel
import com.tsarsprocket.reportmid.baseApi.viewmodel.ViewModelFactoryCreator
import javax.inject.Provider

interface ViewModelFactoryCreatorBinding {
    fun getViewModelFactoryCreators(): Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModelFactoryCreator>>
}