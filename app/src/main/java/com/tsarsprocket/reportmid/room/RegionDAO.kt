package com.tsarsprocket.reportmid.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RegionDAO {

    @Query( "SELECT * FROM regions" )
    fun getAll(): List<RegionEntity>

    @Query( "SELECT * FROM regions WHERE id = :id" )
    fun getById( id: Long ): RegionEntity

    @Query( "SELECT * FROM regions WHERE tag = :tag" )
    fun getByTag( tag: String ): RegionEntity

    @Insert
    fun insert( regionEntity: RegionEntity ): Long

    @Delete
    fun delete( regionEntity: RegionEntity )
}