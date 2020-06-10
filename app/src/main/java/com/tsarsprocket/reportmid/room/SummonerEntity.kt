package com.tsarsprocket.reportmid.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity( tableName = "summoners" )
data class SummonerEntity(
    @ColumnInfo( name = "puuid" ) var puuid: String?
) {

    @PrimaryKey( autoGenerate = true ) var id: Long = 0
}