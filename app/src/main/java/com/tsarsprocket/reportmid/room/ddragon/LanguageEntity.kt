package com.tsarsprocket.reportmid.room.ddragon

import androidx.room.*

@Entity(
    tableName = "ddragon_languages",
    indices =  [Index("version_id", "language", unique = true)],
    foreignKeys = [ForeignKey(entity = VersionEntity::class, parentColumns = ["id"], childColumns = ["version_id"])],
)
class LanguageEntity(

    @ColumnInfo(name = "version_id")
    var versionId: Long,

    @ColumnInfo(name = "language")
    var language: String,
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0
}