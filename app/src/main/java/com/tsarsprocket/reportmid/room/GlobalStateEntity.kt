package com.tsarsprocket.reportmid.room

import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity( tableName = "global_state" )
data class GlobalStateEntity(
    @ColumnInfo( name = "cur_my_account_id" ) var curMyAccountId: Long?
) {

    @ColumnInfo( name = "id" ) @PrimaryKey( autoGenerate = true ) var id: Long = 0
}