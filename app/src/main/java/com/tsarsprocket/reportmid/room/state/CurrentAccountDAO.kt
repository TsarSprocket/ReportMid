package com.tsarsprocket.reportmid.room.state

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Flowable
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
    fun getByRegion(regionId: Long): CurrentAccountEntity?

    @Query("SELECT * FROM state_current_accounts WHERE region_id = :regionId")
    fun getByRegionObservable(regionId: Long): Observable<List<CurrentAccountEntity>>

    @Query("SELECT * FROM state_current_accounts WHERE account_id = :myAccId")
    fun getByMyAccount(myAccId: Long): CurrentAccountEntity?

    @Insert
    fun insert(currentAccountEntity: CurrentAccountEntity): Long

    @Update
    fun update(currentAccountEntity: CurrentAccountEntity)

    @Delete
    fun delete(currentAccountEntity: CurrentAccountEntity): Completable
}