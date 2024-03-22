package com.tsarsprocket.reportmid.lol_room.room

import androidx.room.Dao
import androidx.room.Query

@Dao
interface RegionDAO {

    @Query("SELECT * FROM regions")
    fun getAll(): List<RegionEntity>

    @Query("SELECT * FROM regions WHERE id = :id")
    suspend fun getById(id: Long): RegionEntity

    @Query("SELECT * FROM regions WHERE tag = :tag")
    suspend fun getByTag(tag: String): RegionEntity
}