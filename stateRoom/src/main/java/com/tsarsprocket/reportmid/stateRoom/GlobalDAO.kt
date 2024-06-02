package com.tsarsprocket.reportmid.stateRoom

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface GlobalDAO {

    @Query( "SELECT * FROM state_global" )
    suspend fun getGlobal(): GlobalEntity?

    @Insert
    suspend fun insert(globalEntity: GlobalEntity): Long

    @Update()
    suspend fun update(globalEntity: GlobalEntity)
}
