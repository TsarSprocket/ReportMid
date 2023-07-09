package com.tsarsprocket.reportmid.room.state

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.tsarsprocket.reportmid.room.MyAccountEntity
import com.tsarsprocket.reportmid.room.RegionEntity

@Entity(
    tableName = "state_current_accounts",
    foreignKeys = [
        ForeignKey( entity = RegionEntity::class, parentColumns = [ "id" ], childColumns = [ "region_id" ] ),
        ForeignKey( entity = MyAccountEntity::class, parentColumns = [ "id" ], childColumns = [ "account_id" ] )
    ],
    indices = [
        Index( "region_id", unique = true ),
        Index( "account_id", unique = true )
    ]
)
data class CurrentAccountEntity(
    @NonNull @ColumnInfo( name = "region_id" ) var regionId: Long,
    @NonNull @ColumnInfo( name = "account_id" ) var accountId: Long
) {
    @ColumnInfo( name = "id" ) @PrimaryKey( autoGenerate = true ) var id: Long = 0
}