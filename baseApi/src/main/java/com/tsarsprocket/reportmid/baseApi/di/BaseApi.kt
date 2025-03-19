package com.tsarsprocket.reportmid.baseApi.di

import androidx.fragment.app.FragmentFactory
import com.tsarsprocket.reportmid.baseApi.viewmodel.ViewModelFactoryProvider

interface BaseApi {
    fun getFragmentFactory(): FragmentFactory
    fun getViewModelFactoryProvider(): ViewModelFactoryProvider
}