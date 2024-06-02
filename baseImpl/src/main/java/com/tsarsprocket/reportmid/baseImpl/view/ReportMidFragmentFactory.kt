package com.tsarsprocket.reportmid.baseImpl.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tsarsprocket.reportmid.baseApi.di.qualifiers.Aggregated
import javax.inject.Inject
import javax.inject.Provider

class ReportMidFragmentFactory @Inject constructor(
    @Aggregated
    private val providers: Map<Class<out Fragment>, @JvmSuppressWildcards Provider<Fragment>>
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return providers[classLoader.loadClass(className)]?.get() ?: super.instantiate(classLoader, className)
    }
}