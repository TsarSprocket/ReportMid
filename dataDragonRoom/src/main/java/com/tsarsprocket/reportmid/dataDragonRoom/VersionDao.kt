package com.tsarsprocket.reportmid.dataDragonRoom

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface VersionDao {
    @Query("SELECT * FROM ddragon_versions")
    suspend fun getAll(): List<VersionEntity>

    @Query("SELECT * FROM ddragon_versions WHERE id = :id")
    suspend fun getById(id: Long): VersionEntity?

    @Insert
    suspend fun insert(version: VersionEntity): Long
}