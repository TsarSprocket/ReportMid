package com.tsarsprocket.reportmid.data_dragon_room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "ddragon_versions",
    indices = [Index("version", unique = true)],
)
data class VersionEntity(
    @ColumnInfo(name = "version")
    var version: String,
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0
}