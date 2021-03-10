package com.tsarsprocket.reportmid.room.ddragon

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface SummonerSpellDao {

    @Query("SELECT * FROM ddragon_summoner_spells WHERE language_id = :langId")
    fun getByLanguageId(langId: Long): Observable<List<SummonerSpellEntity>>

    @Query("SELECT * FROM ddragon_summoner_spells WHERE id = :id")
    fun getById(id: Long): Observable<SummonerSpellEntity>

    @Insert
    fun insert(summonerSpellEntity: SummonerSpellEntity): Single<Long>

    @Delete
    fun delete(summonerSpellEntity: SummonerSpellEntity): Single<Int>
}