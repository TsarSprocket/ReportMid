package com.tsarsprocket.reportmid

import android.content.Context
import androidx.fragment.app.Fragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.internal.Preconditions
import javax.inject.Inject

abstract class BaseFragment : Fragment(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> {

        return dispatchingAndroidInjector
    }

    override fun onAttach(context: Context) {

        inject( this, context )

        onPostInject( context )

        super.onAttach(context)
    }

    open fun onPostInject( context: Context ) {}

    fun inject(fragment: Fragment, context: Context ) {

        Preconditions.checkNotNull(fragment, "fragment")

        val hasAndroidInjector = context.applicationContext as? HasAndroidInjector?:
        throw RuntimeException( "Application ${context.applicationContext::class.simpleName} does must implement HasAndroidInjector" )

        hasAndroidInjector.androidInjector().inject( fragment )
    }
}
