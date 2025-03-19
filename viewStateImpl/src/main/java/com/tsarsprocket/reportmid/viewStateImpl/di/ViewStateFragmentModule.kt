package com.tsarsprocket.reportmid.viewStateImpl.di

import androidx.fragment.app.Fragment
import com.tsarsprocket.reportmid.baseApi.di.FragmentKey
import com.tsarsprocket.reportmid.viewStateApi.view.ViewStateFragment
import com.tsarsprocket.reportmid.viewStateImpl.view.ViewStateFragmentImpl
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal interface ViewStateFragmentModule {

    @Binds
    @IntoMap
    @FragmentKey(ViewStateFragment::class)
    fun bindViewStateFragment(fragment: ViewStateFragmentImpl): Fragment

    // This rule is also required because when restored from saved bundle, the fragment is instantiated by its terminal class
    @Binds
    @IntoMap
    @FragmentKey(ViewStateFragmentImpl::class)
    fun bindViewStateFragmentImpl(fragment: ViewStateFragmentImpl): Fragment
}