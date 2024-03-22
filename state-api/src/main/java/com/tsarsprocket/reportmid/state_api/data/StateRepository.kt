package com.tsarsprocket.reportmid.state_api.data

import com.tsarsprocket.reportmid.lol.model.Region
import com.tsarsprocket.reportmid.state_api.model.CurrentAccount

interface StateRepository {
    suspend fun getActiveCurrentAccountId(): Long?
    suspend fun setActiveCurrentAccountId(id: Long)

    suspend fun deleteCurrentAccount(currentAccount: CurrentAccount)
    suspend fun getCurrentAccountById(id: Long): CurrentAccount
    suspend fun getCurrentAccountByMyAccountId(myAccount: Long): CurrentAccount?
    suspend fun getCurrentAccountByRegion(region: Region): CurrentAccount
    suspend fun setCurrentAccountIdByRegion(region: Region, myAccountId: Long): CurrentAccount
}