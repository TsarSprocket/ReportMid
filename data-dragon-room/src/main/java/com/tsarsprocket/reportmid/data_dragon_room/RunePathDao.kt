package com.tsarsprocket.reportmid.data_dragon_room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface RunePathDao {
    @Query("SELECT * FROM ddragon_rune_paths WHERE language_id = :langId")
    fun getByLanguageId(langId: Long): Observable<List<RunePathEntity>>

    @Query("SELECT * FROM ddragon_rune_paths WHERE id = :id")
    fun getById(id: Long): Observable<RunePathEntity>

    @Insert
    fun insert(runePathEntity: RunePathEntity): Single<Long>

    @Delete
    fun delete(runePathEntity: RunePathEntity): Single<Int>
}