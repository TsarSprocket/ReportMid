package com.tsarsprocket.reportmid.room.ddragon

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "ddragon_summoner_spells",
    foreignKeys = [ForeignKey(entity = LanguageEntity::class, parentColumns = ["id"], childColumns = ["language_id"], onDelete = ForeignKey.CASCADE)],
)
class SummonerSpellEntity(
    @ColumnInfo(name = "language_id", index = true) var language_id: Long,
    @ColumnInfo(name = "str_id") var strId: String,
    @ColumnInfo(name = "key") var key: Long,
    @ColumnInfo(name = "image_name") var imageName: String,
) {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Long = 0
}