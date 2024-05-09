package com.tsarsprocket.reportmid.base_api.di

import androidx.fragment.app.FragmentFactory

interface BaseApi {
    fun getFragmentFactory(): FragmentFactory
}