package com.tsarsprocket.reportmid.data_dragon_room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "ddragon_champions",
    foreignKeys = [ForeignKey(entity = LanguageEntity::class, parentColumns = ["id"], childColumns = ["language_id"], onDelete = ForeignKey.CASCADE)],
)
data class ChampionEntity(
    @ColumnInfo(name = "language_id", index = true) var language_id: Long,
    @ColumnInfo(name = "riot_id", index = true) var riotId: Long,
    @ColumnInfo(name = "riot_str_id") var riotStrId: String,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "icon_name") var iconName: String,
) {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Long = 0
}
