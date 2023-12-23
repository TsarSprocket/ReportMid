package com.tsarsprocket.reportmid.data_dragon_room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface VersionDao {
    @Query("SELECT * FROM ddragon_versions")
    fun getAll(): Observable<List<VersionEntity>>

    @Query("SELECT * FROM ddragon_versions WHERE id = :id")
    fun getById(id: Long): Observable<VersionEntity>

    @Query("SELECT count(*) FROM ddragon_versions WHERE version = :ver")
    fun isPresent(ver: String): Observable<Boolean>

    @Insert
    fun insert(version: VersionEntity): Single<Long>

    @Delete
    fun delete(version: VersionEntity): Single<Int>
}