package com.tsarsprocket.reportmid.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MyAccountDAO {

    @Query( "SELECT * FROM my_accounts" )
    fun getAll(): List<MyAccountEntity>

    @Query( "SELECT * FROM my_accounts WHERE id = :id" )
    fun getById( id: Long ): MyAccountEntity

    @Insert
    fun insert( myAccountEntity: MyAccountEntity ): Long

    @Delete
    fun delete( myAccountEntity: MyAccountEntity )
}