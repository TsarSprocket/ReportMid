package com.tsarsprocket.reportmid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate( savedInstanceState: Bundle? ) {

        AndroidInjection.inject( this )

        onPostInject( savedInstanceState )

        super.onCreate(savedInstanceState)
    }

    open fun onPostInject( savedInstanceState: Bundle?)  { }

    override fun androidInjector(): AndroidInjector<Any> {

        return dispatchingAndroidInjector
    }
}
