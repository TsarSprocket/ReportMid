package com.tsarsprocket.reportmid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    open val toolbar: Toolbar get() = throw RuntimeException( "Activity ${this.javaClass.kotlin.simpleName} has no toolbar" )

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
