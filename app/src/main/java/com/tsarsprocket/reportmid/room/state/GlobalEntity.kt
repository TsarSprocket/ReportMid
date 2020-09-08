package com.tsarsprocket.reportmid.room.state

import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.tsarsprocket.reportmid.room.RegionEntity

@Entity(
    tableName = "state_global",
    foreignKeys = [
        ForeignKey( entity = CurrentAccountEntity::class, parentColumns = [ "id" ], childColumns = [ "current_account_id" ] )
    ]
)
data class GlobalEntity(
    @ColumnInfo( name = "current_account_id", index = true ) var currentAccountId: Long?
) {
    @ColumnInfo( name = "id" ) @PrimaryKey( autoGenerate = true ) var id: Long = 0
}