package com.tsarsprocket.reportmid.base.di

import androidx.fragment.app.Fragment
import javax.inject.Provider

fun interface FragmentsCreator {
    fun getFragmentCreators(): Map<Class<out Fragment>, @JvmSuppressWildcards Provider<Fragment>>
}