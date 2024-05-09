package com.tsarsprocket.reportmid.app_impl.di

import androidx.fragment.app.Fragment
import com.tsarsprocket.reportmid.base_api.di.AppScope
import com.tsarsprocket.reportmid.base_api.di.BindingExport
import com.tsarsprocket.reportmid.base_api.di.FragmentsCreator
import com.tsarsprocket.reportmid.base_api.di.qualifiers.Aggregated
import dagger.Module
import dagger.Provides
import javax.inject.Provider

@Module
interface AggregatorModule {

    companion object {

        @Provides
        @AppScope
        @Aggregated
        fun provideFragmentCreators(@BindingExport bindingExports: @JvmSuppressWildcards Set<Any>): Map<Class<out Fragment>, Provider<Fragment>> {
            return bindingExports.filterIsInstance<FragmentsCreator>()
                .fold(emptyMap()) { acc, entry -> acc + entry.getFragmentCreators() }
        }
    }
}