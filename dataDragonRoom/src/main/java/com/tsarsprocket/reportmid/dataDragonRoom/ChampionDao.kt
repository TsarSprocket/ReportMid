package com.tsarsprocket.reportmid.dataDragonRoom

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ChampionDao {

    @Query("SELECT * FROM ddragon_champions WHERE language_id = :langId")
    suspend fun getByLangueageId(langId: Long): List<ChampionEntity>

    @Insert
    suspend fun insert(championEntity: ChampionEntity): Long
}