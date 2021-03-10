package com.tsarsprocket.reportmid.room.ddragon

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "ddragon_items",
    foreignKeys = [ForeignKey(entity = LanguageEntity::class, parentColumns = ["id"], childColumns = ["language_id"], onDelete = ForeignKey.CASCADE)],
)
data class ItemEntity(
    @ColumnInfo(name = "language_id", index = true) var language_id: Long,
    @ColumnInfo(name = "riotId") var riotId: Long,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "imageName") var imageName: String,
) {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Long = 0
}
