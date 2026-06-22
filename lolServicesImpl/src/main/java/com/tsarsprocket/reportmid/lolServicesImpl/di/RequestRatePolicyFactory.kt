package com.tsarsprocket.reportmid.lolServicesImpl.di

import com.tsarsprocket.reportmid.lolServicesImpl.riotapi.RequestRatePolicy
import dagger.assisted.AssistedFactory

@AssistedFactory
internal interface RequestRatePolicyFactory {
    fun create(rate: Int, intervalMillis: Long): RequestRatePolicy
}