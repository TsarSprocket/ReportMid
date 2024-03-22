package com.tsarsprocket.reportmid.state_impl.data

import com.tsarsprocket.reportmid.app_api.room.MainStorage
import com.tsarsprocket.reportmid.base.di.qualifiers.Io
import com.tsarsprocket.reportmid.lol.model.Region
import com.tsarsprocket.reportmid.state_api.data.MyAccountNotFoundException
import com.tsarsprocket.reportmid.state_api.data.StateRepository
import com.tsarsprocket.reportmid.state_api.model.CurrentAccount
import com.tsarsprocket.reportmid.state_room.CurrentAccountEntity
import com.tsarsprocket.reportmid.state_room.GlobalEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StateRepositoryImpl @Inject constructor(
    private val storage: MainStorage,
    @Io private val ioDispatcher: CoroutineDispatcher,
) : StateRepository {

    override suspend fun deleteCurrentAccount(currentAccount: CurrentAccount) = withContext(ioDispatcher) {
        storage.currentAccountDAO().delete(
            CurrentAccountEntity(
                currentAccount.region.id,
                currentAccount.myAccountId,
            ).apply { id = currentAccount.id }
        )
    }

    override suspend fun getActiveCurrentAccountId(): Long? = withContext(ioDispatcher) {
        storage.globalDAO().getAll().firstOrNull()?.currentAccountId
    }

    override suspend fun getCurrentAccountById(id: Long): CurrentAccount = withContext(ioDispatcher) {
        storage.currentAccountDAO().getById(id).run { CurrentAccount(id, Region.getById(regionId), accountId) }
    }

    override suspend fun getCurrentAccountByMyAccountId(myAccountId: Long): CurrentAccount? = withContext(ioDispatcher) {
        storage.currentAccountDAO().getByMyAccountId(myAccountId)?.run { CurrentAccount(id, Region.getById(regionId), myAccountId) }
    }

    override suspend fun getCurrentAccountByRegion(region: Region): CurrentAccount = withContext(ioDispatcher) {
        storage.currentAccountDAO().getByRegionId(region.id)?.run {
            CurrentAccount(
                id = id,
                region = region,
                myAccountId = accountId,
            )
        } ?: throw MyAccountNotFoundException("region = ${region.title}")
    }

    override suspend fun setActiveCurrentAccountId(newCurrentAccountId: Long) {
        with(storage.globalDAO()) {
            val entity = getAll().firstOrNull()
            if(entity != null) {
                entity.currentAccountId = newCurrentAccountId
                update(entity)
            } else {
                insert(GlobalEntity(newCurrentAccountId))
            }
        }
    }

    override suspend fun setCurrentAccountIdByRegion(region: Region, myAccountId: Long): CurrentAccount = withContext(ioDispatcher) {
        storage.currentAccountDAO().getByRegionId(region.id)?.run {
            (accountId != myAccountId).also { notTheSame ->
                if(notTheSame) {
                    accountId = myAccountId
                    storage.currentAccountDAO().update(this)
                }
            }
            CurrentAccount(id, Region.getById(regionId), myAccountId)
        } ?: with(CurrentAccountEntity(region.id, myAccountId)) {
            CurrentAccount(storage.currentAccountDAO().insert(this), region, myAccountId)
        }
    }
}