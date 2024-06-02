package com.tsarsprocket.reportmid.dataDragonRoom

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RunePathDao {

    @Query("SELECT * FROM ddragon_rune_paths WHERE language_id = :langId")
    suspend fun getByLanguageId(langId: Long): List<RunePathEntity>

    @Insert
    suspend fun insert(runePathEntity: RunePathEntity): Long
}