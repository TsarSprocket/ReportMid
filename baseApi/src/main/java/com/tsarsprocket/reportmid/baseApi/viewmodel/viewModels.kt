package com.tsarsprocket.reportmid.baseApi.viewmodel

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras

inline fun <reified VM : ViewModel> Fragment.viewModels(
    factoryProvider: ViewModelFactoryProvider,
    noinline ownerProducer: () -> ViewModelStoreOwner = { this },
    noinline extrasProducer: (() -> CreationExtras)? = null,
) = viewModels<VM>(ownerProducer, extrasProducer) { factoryProvider.provide(VM::class.java, this) }