package com.tsarsprocket.reportmid.summoner_room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SummonerDAO {

    @Query("SELECT * FROM summoners WHERE id = :id")
    suspend fun getById(id: Long): SummonerEntity

    @Query("SELECT * FROM summoners WHERE puuid = :puuid AND region_id = :regionId")
    suspend fun getByPuuidAndRegionId(puuid: String, regionId: Long): SummonerEntity

    @Query("""
        SELECT s.*
        FROM summoners AS s
            JOIN my_accounts ma ON s.id = ma.summoner_id
        WHERE s.region_id = :regionId
    """)
    suspend fun getMySummonersByRegion(regionId: Long): List<SummonerEntity>

    @Query("""
        SELECT s.*
        FROM summoners AS s
            JOIN my_accounts ma ON s.id = ma.summoner_id
    """)
    suspend fun getMySummoners(): List<SummonerEntity>

    @Insert
    suspend fun insert(summonerEntity: SummonerEntity): Long

    @Delete
    suspend fun delete(summonerEntity: SummonerEntity)
}