package com.tsarsprocket.reportmid.stateApi.di

import com.tsarsprocket.reportmid.stateApi.data.StateRepository

interface StateApi {
    fun getStateRepository(): StateRepository
}