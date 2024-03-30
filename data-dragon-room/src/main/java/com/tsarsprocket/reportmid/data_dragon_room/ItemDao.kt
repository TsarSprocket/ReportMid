package com.tsarsprocket.reportmid.data_dragon_room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ItemDao {

    @Query("SELECT * FROM ddragon_items WHERE language_id = :langId")
    suspend fun getByLanguageId(langId: Long): List<ItemEntity>

    @Insert
    suspend fun insert(itemEntity: ItemEntity): Long
}