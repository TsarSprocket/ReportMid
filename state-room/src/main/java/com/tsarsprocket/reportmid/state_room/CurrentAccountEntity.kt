package com.tsarsprocket.reportmid.state_room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "state_current_accounts",
    indices = [
        Index( "region_id", unique = true ),
        Index( "account_id", unique = true )
    ]
)
data class CurrentAccountEntity(
    @ColumnInfo(name = "region_id") var regionId: Long,
    @ColumnInfo(name = "account_id") var accountId: Long
) {
    @ColumnInfo( name = "id" ) @PrimaryKey( autoGenerate = true ) var id: Long = 0
}