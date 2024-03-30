package com.tsarsprocket.reportmid.data_dragon_room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Observable

@Dao
interface VersionDao {
    @Query("SELECT * FROM ddragon_versions")
    fun getAll(): Observable<List<VersionEntity>>

    @Query("SELECT * FROM ddragon_versions WHERE id = :id")
    suspend fun getById(id: Long): VersionEntity?

    @Insert
    suspend fun insert(version: VersionEntity): Long
}