package com.tsarsprocket.reportmid.stateRoom

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CurrentAccountDAO {

    @Query("SELECT * FROM state_current_accounts WHERE id = :id")
    suspend fun getById(id: Long): CurrentAccountEntity?

    @Query("SELECT * FROM state_current_accounts WHERE region_id = :regionId")
    suspend fun getByRegionId(regionId: Long): CurrentAccountEntity?

    @Query("SELECT * FROM state_current_accounts WHERE account_id = :myAccId")
    suspend fun getByMyAccountId(myAccId: Long): CurrentAccountEntity?

    @Insert
    suspend fun insert(currentAccountEntity: CurrentAccountEntity): Long

    @Update
    suspend fun update(currentAccountEntity: CurrentAccountEntity)

    @Delete
    suspend fun delete(currentAccountEntity: CurrentAccountEntity)
}