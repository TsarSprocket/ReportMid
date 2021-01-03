package com.tsarsprocket.reportmid.room.ddragon

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "ddragon_rune_paths",
    foreignKeys = [ForeignKey(entity = LanguageEntity::class, parentColumns = ["id"], childColumns = ["language_id"], onDelete = ForeignKey.CASCADE)],
)
data class RunePathEntity(
    @ColumnInfo(name = "language_id", index = true) var language_id: Long,
    @ColumnInfo(name = "riot_id") var riot_id: Int,
    @ColumnInfo(name = "key") var key: String,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "icon_path") var iconPath: String,
) {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Long = 0
}