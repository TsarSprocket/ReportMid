package com.tsarsprocket.reportmid.room

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable

@Dao
interface MyAccountDAO {

    @Query("SELECT * FROM my_accounts")
    fun getAll(): List<MyAccountEntity>

    @Query("SELECT * FROM my_accounts")
    fun getAllObservable(): Observable<List<MyAccountEntity>>

    @Query("SELECT * FROM my_accounts WHERE id = :id")
    fun getById(id: Long): MyAccountEntity

    @Query("SELECT * FROM my_accounts WHERE summoner_id = :summonerId")
    fun getBySummonerId(summonerId: Long): MyAccountEntity

    @Query("""
        SELECT ma.*
        FROM my_accounts AS ma
            JOIN summoners AS s ON ma.summoner_id = s.id
        WHERE s.region_id = :regId
    """)
    fun getByRegion(regId: Long): List<MyAccountEntity>

    @Query("SELECT count(*) FROM my_accounts")
    fun count(): Int

    @Insert
    fun insert(myAccountEntity: MyAccountEntity): Long

    @Delete
    fun delete(myAccountEntity: MyAccountEntity): Completable
}