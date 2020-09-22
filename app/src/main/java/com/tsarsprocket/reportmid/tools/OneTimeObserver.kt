package com.tsarsprocket.reportmid.tools

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

abstract class OneTimeObserver<T>: Observer<T> {

    var liveData: LiveData<T>? = null

    final override fun onChanged(t: T) {
        liveData?.let {
            it.removeObserver(this)
            onOneTimeChanged(t)
        }
    }

    abstract fun onOneTimeChanged(v: T)

    fun observeOn(ld: LiveData<T>, lifecycleOwner: LifecycleOwner) {
        if(liveData != null) throw RuntimeException("${javaClass.kotlin.simpleName} instance can only be used once")

        liveData = ld
        ld.observe(lifecycleOwner,this)
    }
}