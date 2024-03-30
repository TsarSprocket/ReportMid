package com.tsarsprocket.reportmid.data_dragon_room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RuneDao {

    @Query("SELECT * FROM ddragon_runes WHERE rune_path_id = :runePathId")
    suspend fun getByRunePathId(runePathId: Long): List<RuneEntity>

    @Insert
    suspend fun insert(runeEntity: RuneEntity): Long
}