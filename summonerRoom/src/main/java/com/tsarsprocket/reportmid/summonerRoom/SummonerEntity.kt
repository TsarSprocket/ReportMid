package com.tsarsprocket.reportmid.summonerRoom

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = SummonerEntity.TABLE_NAME,
    indices = [
        Index(value = [SummonerEntity.COLUMN_PUUID, SummonerEntity.COLUMN_REGION_ID], unique = true)
    ]
)
data class SummonerEntity(
    @ColumnInfo(name = COLUMN_PUUID) var puuid: String,
    @ColumnInfo(name = COLUMN_REGION_ID, index = true) var regionId: Long
) {
    @ColumnInfo( name = "id" ) @PrimaryKey( autoGenerate = true ) var id: Long = 0

    companion object {
        const val TABLE_NAME = "summoners"
        const val COLUMN_PUUID = "puuid"
        const val COLUMN_REGION_ID = "region_id"
    }
}