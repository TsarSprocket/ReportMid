package com.tsarsprocket.reportmid.lol_room.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "regions")
data class RegionEntity(
    @ColumnInfo(name = "tag", index = true)
    var tag: String,

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = false)
    var id: Long = 0,
)
