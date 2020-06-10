package com.tsarsprocket.reportmid.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface GlobalStateDAO {

    @Query( "SELECT * FROM global_state" )
    fun getAll(): List<GlobalStateEntity>

    @Insert
    fun insert( globalStateEntity: GlobalStateEntity ): Long

    @Update()
    fun update( globalStateEntity: GlobalStateEntity )
}