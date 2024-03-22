package com.tsarsprocket.reportmid.state_api.di

import com.tsarsprocket.reportmid.state_api.data.StateRepository

interface StateApi {
    fun getStateRepository(): StateRepository
}