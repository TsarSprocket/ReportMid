package com.tsarsprocket.reportmid.room

import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity( tableName = "global_state" )
data class GlobalStateEntity(
    @ColumnInfo( name = "cur_summoner_id", defaultValue = "-1" ) var curSummonerId: Long
) {

    @PrimaryKey( autoGenerate = true ) var id: Long = 0
}