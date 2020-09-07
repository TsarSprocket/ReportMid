package com.tsarsprocket.reportmid.room

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity( tableName = "regions" )
data class RegionEntity(
    @NonNull @ColumnInfo( name = "tag", index = true ) var tag: String
) {
    @ColumnInfo( name = "id" ) @PrimaryKey( autoGenerate = true ) var id: Long = 0
}