package com.tsarsprocket.reportmid.data_dragon_room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface LanguageDao {
    @Query("SELECT * FROM ddragon_languages WHERE version_id = :versionId")
    fun getAllForVersion(versionId: Long): Observable<List<LanguageEntity>>

    @Query("SELECT * FROM ddragon_languages WHERE id = (SELECT max(id) FROM ddragon_languages)")
    fun getLatestLanguage(): Observable<List<LanguageEntity>>

    @Insert
    fun insert(language: LanguageEntity): Single<Long>

    @Delete
    fun delete(language: LanguageEntity): Single<Int>
}