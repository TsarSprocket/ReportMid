package com.tsarsprocket.reportmid.summoner_room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MyAccountDAO {

    @Query("SELECT * FROM my_accounts")
    suspend fun getAll(): List<MyAccountEntity>

    @Query("SELECT * FROM my_accounts WHERE id = :id")
    suspend fun getById(id: Long): MyAccountEntity

    @Query("SELECT * FROM my_accounts WHERE summoner_id = :summonerId")
    suspend fun getBySummonerId(summonerId: Long): MyAccountEntity

    @Query(
        """
        SELECT ma.*
        FROM my_accounts AS ma
        JOIN summoners AS s ON ma.summoner_id = s.id
        WHERE s.puuid = :puuid AND s.region_id = :regionId
        """
    )
    suspend fun getByPuuidAndRegionId(puuid: String, regionId: Long): MyAccountEntity

    @Query("""
        SELECT ma.*
        FROM my_accounts AS ma
            JOIN summoners AS s ON ma.summoner_id = s.id
        WHERE s.region_id = :regId
    """)
    suspend fun getByRegion(regId: Long): List<MyAccountEntity>

    @Query("SELECT count(*) FROM my_accounts")
    suspend fun count(): Int

    @Insert
    suspend fun insert(myAccountEntity: MyAccountEntity): Long

    @Delete
    suspend fun delete(myAccountEntity: MyAccountEntity)
}