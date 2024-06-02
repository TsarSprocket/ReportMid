package com.tsarsprocket.reportmid.dataDragonRoom

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "ddragon_runes",
    foreignKeys = [ForeignKey(entity = RunePathEntity::class, parentColumns = ["id"], childColumns = ["rune_path_id"])],
)
class RuneEntity(
    @ColumnInfo(name = "rune_path_id", index = true) var runePathId: Long,
    @ColumnInfo(name = "riot_id") var riotId: Int,
    @ColumnInfo(name = "key") var key: String,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "icon_path") var iconPath: String,
    @ColumnInfo(name = "slot_no") var slotNo: Int,
) {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Long = 0
}