package com.tsarsprocket.reportmid.base_impl.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tsarsprocket.reportmid.base_api.di.qualifiers.Aggregated
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