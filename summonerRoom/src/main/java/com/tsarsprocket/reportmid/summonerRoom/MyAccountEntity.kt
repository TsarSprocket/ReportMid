package com.tsarsprocket.reportmid.summonerRoom

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "my_accounts",
    foreignKeys = [
        ForeignKey( entity = SummonerEntity::class, parentColumns = [ "id" ], childColumns = [ "summoner_id" ] )
    ],
    indices = [
        Index( "summoner_id", unique = true )
    ]
)
data class MyAccountEntity(
    @ColumnInfo(name = "summoner_id") var summonerId: Long
) {
    @PrimaryKey( autoGenerate = true ) var id: Long = 0
}