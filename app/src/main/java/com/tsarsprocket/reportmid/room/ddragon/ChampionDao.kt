package com.tsarsprocket.reportmid.room.ddragon

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface ChampionDao {
    @Query("SELECT * FROM ddragon_champions WHERE id = :id")
    fun getById(id: Long): Observable<ChampionEntity>

    @Query("SELECT * FROM ddragon_champions WHERE language_id = :langId")
    fun getByLangueageId(langId: Long): Observable<List<ChampionEntity>>

    @Insert
    fun insert(championEntity: ChampionEntity): Single<Long>

    @Delete
    fun delete(championEntity: ChampionEntity): Single<Int>
}