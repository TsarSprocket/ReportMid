package com.tsarsprocket.reportmid.view_state_impl.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.tsarsprocket.reportmid.base.di.FragmentKey
import com.tsarsprocket.reportmid.base.di.PerApi
import com.tsarsprocket.reportmid.base.di.ViewModelKey
import com.tsarsprocket.reportmid.base.viewmodel.ViewModelFactory
import com.tsarsprocket.reportmid.view_state_api.view.ViewStateFragment
import com.tsarsprocket.reportmid.view_state_impl.view.ViewStateFragmentImpl
import com.tsarsprocket.reportmid.view_state_impl.viewmodel.ViewStateViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Provider

@Module
internal interface ViewStateModule {

    @Binds
    @IntoMap
    @FragmentKey(ViewStateFragment::class)
    fun bindViewStateFragment(fragment: ViewStateFragmentImpl): Fragment

    @Binds
    @IntoMap
    @ViewModelKey(ViewStateViewModel::class)
    fun bindViewStateViewModel(viewModel: ViewStateViewModel): ViewModel

    companion object {

        @Provides
        @PerApi
        fun provideViewModelFactory(creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>): ViewModelFactory {
            return ViewModelFactory(creators)
        }
    }
}