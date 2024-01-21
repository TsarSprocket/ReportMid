package com.tsarsprocket.reportmid.summoner_room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Observable

@Dao
interface SummonerDAO {

    @Query("SELECT * FROM summoners")
    fun getAll(): List<SummonerEntity>

    @Query("SELECT * FROM summoners WHERE id = :id")
    fun getById(id: Long): SummonerEntity

    @Query("SELECT * FROM summoners WHERE puuid = :puuid AND region_id = :regionId")
    fun getByPuuidAndRegionId(puuid: String, regionId: Long): SummonerEntity?

    @Query("""
        SELECT s.* 
        FROM summoners AS s
            JOIN regions AS r ON s.region_id = r.id
        WHERE s.puuid = :puuid AND r.tag = :regionTag
    """)
    fun getByPuuidAndRegionTag(puuid: String, regionTag: String): SummonerEntity?

    @Query("""
        SELECT s.*
        FROM summoners AS s
            JOIN my_accounts ma ON s.id = ma.summoner_id
        WHERE s.region_id = :regionId
    """)
    fun getMySummonersByRegionObservable(regionId: Long): Observable<List<SummonerEntity>>

    @Query("""
        SELECT s.*
        FROM summoners AS s
            JOIN my_accounts ma ON s.id = ma.summoner_id
    """)
    fun getMySummoners(): Observable<List<SummonerEntity>>

    @Insert
    fun insert(summonerEntity: SummonerEntity): Long

    @Delete
    fun delete(summonerEntity: SummonerEntity)
}