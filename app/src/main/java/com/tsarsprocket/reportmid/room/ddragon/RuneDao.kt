package com.tsarsprocket.reportmid.room.ddragon

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface RuneDao {

    @Query("SELECT * FROM ddragon_runes WHERE rune_path_id = :runePathId")
    fun getByRunePathId(runePathId: Long): Observable<List<RuneEntity>>

    @Query("SELECT * FROM ddragon_runes WHERE id = :id")
    fun getById(id: Long): Observable<RuneEntity>

    @Insert
    fun insert(runeEntity: RuneEntity): Single<Long>

    @Delete
    fun delete(runeEntity: RuneEntity): Single<Int>
}