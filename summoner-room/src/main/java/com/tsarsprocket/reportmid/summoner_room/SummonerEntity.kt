package com.tsarsprocket.reportmid.summoner_room

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.tsarsprocket.reportmid.lol_room.room.RegionEntity

@Entity(
    tableName = "summoners",
    foreignKeys = [
        ForeignKey( entity = RegionEntity::class, parentColumns = [ "id" ], childColumns = [ "region_id" ] )
    ]
)
data class SummonerEntity(
    @NonNull @ColumnInfo( name = "puuid" ) var puuid: String,
    @NonNull @ColumnInfo( name = "region_id", index = true ) var regionId: Long
) {
    @ColumnInfo( name = "id" ) @PrimaryKey( autoGenerate = true ) var id: Long = 0
}