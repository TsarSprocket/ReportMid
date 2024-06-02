package com.tsarsprocket.reportmid.dataDragonRoom

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SummonerSpellDao {

    @Query("SELECT * FROM ddragon_summoner_spells WHERE language_id = :langId")
    suspend fun getByLanguageId(langId: Long): List<SummonerSpellEntity>

    @Insert
    suspend fun insert(summonerSpellEntity: SummonerSpellEntity): Long
}