package com.tsarsprocket.reportmid.summonerRoom

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "summoners",
)
data class SummonerEntity(
    @NonNull @ColumnInfo( name = "puuid" ) var puuid: String,
    @NonNull @ColumnInfo( name = "region_id", index = true ) var regionId: Long
) {
    @ColumnInfo( name = "id" ) @PrimaryKey( autoGenerate = true ) var id: Long = 0
}