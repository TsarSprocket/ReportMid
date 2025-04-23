package com.tsarsprocket.reportmid.baseApi.di

import androidx.fragment.app.Fragment
import javax.inject.Provider

interface FragmentCreatorBinding {
    fun getFragmentCreators(): Map<Class<out Fragment>, @JvmSuppressWildcards Provider<Fragment>>
}
