package com.tsarsprocket.reportmid.app.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tsarsprocket.reportmid.base.di.AppScope
import javax.inject.Inject
import javax.inject.Provider

@AppScope
class ReportMidFragmentFactory @Inject constructor(
    private val providers: Map<Class<out Fragment>, @JvmSuppressWildcards Provider<Fragment>>
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return providers[classLoader.loadClass(className)]?.get() ?: super.instantiate(classLoader, className)
    }
}