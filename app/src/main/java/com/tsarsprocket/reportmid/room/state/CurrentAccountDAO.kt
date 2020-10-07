package com.tsarsprocket.reportmid.room.state

import androidx.room.*
import io.reactivex.Observable

@Dao
interface CurrentAccountDAO {

    @Query("SELECT * FROM state_current_accounts")
    fun getAll(): List<CurrentAccountEntity>

    @Query("SELECT * FROM state_current_accounts")
    fun getAllObservable(): Observable<List<CurrentAccountEntity>>

    @Query("SELECT * FROM state_current_accounts WHERE id = :id")
    fun getById(id: Long): CurrentAccountEntity

    @Query("SELECT * FROM state_current_accounts WHERE region_id = :regionId")
    fun getByRegionId(regionId: Long): CurrentAccountEntity?

    @Query("SELECT * FROM state_current_accounts WHERE region_id = :regionId")
    fun getByRegionIdObservable(regionId: Long): Observable<List<CurrentAccountEntity>>

    @Query("SELECT * FROM state_current_accounts WHERE account_id = :myAccId")
    fun getByMyAccountId(myAccId: Long): CurrentAccountEntity?

    @Insert
    fun insert(currentAccountEntity: CurrentAccountEntity): Long

    @Update
    fun update(currentAccountEntity: CurrentAccountEntity)

    @Delete
    fun delete(currentAccountEntity: CurrentAccountEntity)
}