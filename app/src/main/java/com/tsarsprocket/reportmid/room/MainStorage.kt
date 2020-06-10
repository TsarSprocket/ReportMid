package com.tsarsprocket.reportmid.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database( entities = [ GlobalStateEntity::class, SummonerEntity::class ], version = 1 )
abstract class MainStorage: RoomDatabase() {

    abstract fun globalStateDAO(): GlobalStateDAO

    abstract fun summonerDAO(): SummonerDAO
}