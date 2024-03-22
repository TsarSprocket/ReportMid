package com.tsarsprocket.reportmid.state_room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface GlobalDAO {

    @Query( "SELECT * FROM state_global" )
    fun getAll(): List<GlobalEntity>

    @Insert
    fun insert(globalEntity: GlobalEntity): Long

    @Update()
    fun update(globalEntity: GlobalEntity)
}
