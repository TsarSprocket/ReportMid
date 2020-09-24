package com.tsarsprocket.reportmid.room.state

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.reactivex.Flowable
import io.reactivex.Observable

@Dao
interface CurrentAccountDAO {

    @Query( "SELECT * FROM state_current_accounts" )
    fun getAll(): List<CurrentAccountEntity>

    @Query( "SELECT * FROM state_current_accounts" )
    fun getAllObservable(): Observable<List<CurrentAccountEntity>>

    @Query( "SELECT * FROM state_current_accounts WHERE id = :id" )
    fun getById( id: Long ): CurrentAccountEntity

    @Query( "SELECT * FROM state_current_accounts WHERE region_id = :regionId" )
    fun getByRegion( regionId: Long ): CurrentAccountEntity?

    @Insert
    fun insert( currentAccountEntity: CurrentAccountEntity ): Long

    @Update()
    fun update( currentAccountEntity: CurrentAccountEntity )
}