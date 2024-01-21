package com.tsarsprocket.reportmid.room

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.tsarsprocket.reportmid.my_account_room.MyAccountEntity
import com.tsarsprocket.reportmid.summoner_room.SummonerEntity

@Entity(
    tableName = "my_friends",
    foreignKeys = [
        ForeignKey( entity = MyAccountEntity::class, parentColumns = [ "id" ], childColumns = [ "my_account_id" ] ),
        ForeignKey( entity = SummonerEntity::class, parentColumns = [ "id" ], childColumns = [ "summoner_id" ] )
    ]
)
data class MyFriendEntity(
    @NonNull @ColumnInfo( name = "my_account_id", index = true ) var myAccountId: Long,
    @NonNull @ColumnInfo( name = "summoner_id", index = true ) var summonerId: Long
) {
    @PrimaryKey( autoGenerate = true ) var id: Long = 0
}