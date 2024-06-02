package com.tsarsprocket.reportmid.dataDragonRoom

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LanguageDao {
    @Query("SELECT * FROM ddragon_languages WHERE version_id = :versionId")
    suspend fun getAllForVersion(versionId: Long): List<LanguageEntity>

    @Query("SELECT * FROM ddragon_languages WHERE id = (SELECT max(id) FROM ddragon_languages)")
    suspend fun getLatestLanguage(): LanguageEntity?

    @Insert
    suspend fun insert(language: LanguageEntity): Long
}