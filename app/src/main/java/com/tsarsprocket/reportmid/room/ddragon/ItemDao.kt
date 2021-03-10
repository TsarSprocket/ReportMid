package com.tsarsprocket.reportmid.room.ddragon

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface ItemDao {

    @Query("SELECT * FROM ddragon_items WHERE language_id = :langId")
    fun getByLanguageId(langId: Long): Observable<List<ItemEntity>>

    @Query("SELECT * FROM ddragon_items WHERE id = :id")
    fun getById(id: Long): Observable<ItemEntity>

    @Insert
    fun insert(itemEntity: ItemEntity): Single<Long>

    @Delete
    fun delete(itemEntity: ItemEntity): Single<Int>
}